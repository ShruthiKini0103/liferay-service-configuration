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
                    def script = load 'deployBuilds.groovy'
                    script.deployBuilds()
                }
            }
        }
    }
}
