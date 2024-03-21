pipeline {
    agent {
        node {
            label 'built-in'
        }
    }
    triggers {
        githubPush()
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
                    def script = load 'deployBuilds.groovy'
                    script.deployBuilds()
                }
            }
        }
    }
}
