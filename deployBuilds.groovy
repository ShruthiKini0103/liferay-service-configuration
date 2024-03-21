import groovy.json.JsonSlurper

def deployBuilds(){
    def parallelStagesMap = getEnvironmentList().collectEntries {
        ["${it}" : generateStage(it)]
    }
    
    parallel parallelStagesMap
}

def generateStage(String project) {
    return {
        stage("stage: ${project}") {
            echo "Promoting build to ${project}."
            script {
                def buildNumber = getProjectBuildNumber(project)
                if(buildNumber) {
                    promoteBuild(project, buildNumber) 
                } else {
                    throw new Exception("No build found for 7.4-next branch")
                }    
            }
        }
    }
}

@NonCPS
def isSubstringInList(List<String> list, String string){
   for(String substring : list){
        if(string.contains(substring)){
            return true
        }
   }

   return false
}

@NonCPS
def getEnvironmentList() {
    def json_slurper = new JsonSlurper()
    def environments = []
    List<String> blacklist = new ArrayList<String>()

    if(env.MODL_DEPLOY_BLACKLIST != null && ! env.MODL_DEPLOY_BLACKLIST.equals("")){
        blacklist = Arrays.asList(env.MODL_DEPLOY_BLACKLIST.split(","));
    }

    for(project in json_slurper.parseText(queryLiferayAPI("https://api.liferay.cloud/projects"))){
        def projectId = project.get("projectId")

        if(projectId.contains("modl") && !isSubstringInList(blacklist, projectId)){
            environments.add(projectId)
        }
    }

    return environments
}

@NonCPS
def getProjectBuildNumber(String project) {

    def buildId
    def rootProject = project.split('-')[0]
    def url = "https://api.liferay.cloud/projects/" + rootProject + "-" + rootProject.replace("lxc", "") + "prd/builds?limit=25&shouldGroup=true&serviceId=search&status=SUCCEEDED"
    def json_slurper = new JsonSlurper()
    def json = json_slurper.parseText(queryLiferayAPI(url))

    for(def i = 0; i < json.size(); i++){
        def metadata = json.get(i).get("metadata")

        if (metadata.get("commitBranch").equals("7.4-next") && metadata.get("commitMessage").contains("Updating DXP image to liferay/dxp")){
            buildId = json.get(i).get("buildGroupUid")
            println "7.4-next build: " + buildId
            break
        }
    }

    return buildId
}

@NonCPS
def promoteBuild(String project, String buildId){

    def url = "https://api.liferay.cloud/projects/" + project + "/deploy"
    println url
    def URL deployUrl = new URL(url);
    
    def deployConnection = (HttpURLConnection)deployUrl.openConnection();

    def credentials = env.LCP_CLI_USER + ":" + env.LCP_CLI_PASSWORD
    def postData = '{"buildGroupUid": "' + buildId + '"}'
    println postData

    deployConnection.setRequestProperty("Authorization", "Basic " + credentials.bytes.encodeBase64().toString());
    deployConnection.setRequestProperty("Content-Type", "application/json")
    deployConnection.setRequestMethod("POST");
    deployConnection.setDoOutput(true)
    deployConnection.getOutputStream().write(postData.bytes)

    if (deployConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
        println "Deploy started successfully!"
    } else {
        println "Deploy request failed with status ${deployConnection.getResponseCode()}"
    }

    deployConnection.disconnect()
}

@NonCPS
def queryLiferayAPI(String url) {
   def URL liferayURL = new URL(url);

   def liferayConnection = (HttpURLConnection)liferayURL.openConnection();

   def credentials = env.LCP_CLI_USER + ":" + env.LCP_CLI_PASSWORD

   liferayConnection.setRequestProperty ("Authorization", "Basic " + credentials.bytes.encodeBase64().toString());

   return liferayConnection.getContent().getText()
}

return this
