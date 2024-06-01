pipeline {
    agent any

//    environment {
//        EMAIL_TO = "tsaadimas@hua.gr"
//    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'devops-backend', url: 'git@github.com:VasileiosKokki/FarmerCompensation_University.git'
            }
        }
        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }
        stage('run ansible pipeline') {
            steps {
                build job: 'ansible'
            }
        }
        stage('install ansible prerequisites') {
            steps {
                sh '''
                    ansible-galaxy install geerlingguy.postgresql
                '''
            }
        }
        stage('Install postgres') {
            steps {
                sh '''
                    export ANSIBLE_CONFIG=~/workspace/ansible/ansible.cfg
                    ansible-playbook -i ~/workspace/ansible/hosts.yaml -l azure-db-server ~/workspace/ansible/playbooks/postgres.yaml
                '''
            }
        }

        stage('Deploy spring boot app') {
            steps {
                sh '''
                   # replace dbserver in host_vars
                    # sed -i 's/dbserver/13.74.185.80/g' ~/workspace/ansible/host_vars/appserver-vm.yaml
                   # replace workingdir in host_vars
                    # sed -i 's/vagrant/azureuser/g' ~/workspace/ansible/host_vars/appserver-vm.yaml
                '''
                sh '''
                    # edit host var for appserver

                    export ANSIBLE_CONFIG=~/workspace/ansible/ansible.cfg
                    ansible-playbook -i ~/workspace/ansible/hosts.yaml -l appserver-vm ~/workspace/ansible/playbooks/spring_(without_nginx).yaml
                '''
            }
        }
       stage('Deploy frontend') {
            steps {
                sh '''
                    sed -i 's/dbserver/13.74.185.80/g' ~/workspace/ansible/host_vars/appserver-vm.yaml
                    export ANSIBLE_CONFIG=~/workspace/ansible/ansible.cfg
                    ansible-playbook -i ~/workspace/ansible/hosts.yaml -l appserver-vm -e branch=devops-frontend -e backend_server_url=http://localhost:9090 ~/workspace/ansible/playbooks/vuejs.yaml
                '''
            }
       }
    }


//    post {
//        always {
//            mail  to: "${EMAIL_TO}", body: "Project ${env.JOB_NAME} <br>, Build status ${currentBuild.currentResult} <br> Build Number: ${env.BUILD_NUMBER} <br> Build URL: ${env.BUILD_URL}", subject: "JENKINS: Project name -> ${env.JOB_NAME}, Build -> ${currentBuild.currentResult}"
//        }
//    }
}