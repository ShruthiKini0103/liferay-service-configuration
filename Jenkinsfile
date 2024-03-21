pipeline {
    agent {
        node {
            label 'built-in'
        }
    }
    triggers {
        cron('45 3 * * 5')
    }
    stages {
        stage ('Checkout SCM') {
            steps {
                checkout scm
            }
        }
        stage ('Deploy to Cloud') {
            steps {
                script {
                    def script = load 'automation/jobs/lxc-modl-deployment/deployBuilds.groovy'
                    script.deployBuilds()
                }
            }
        }
    }
}