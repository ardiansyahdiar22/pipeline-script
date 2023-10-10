pipeline {
    agent any
    
    parameters {
        string(name: 'BRANCH', defaultValue: 'local-dev', description: 'Git branch to checkout')
        string(name: 'TAG', defaultValue: '@login_on_web_app', description: 'Behave tags to run')
    }

    environment {
        PYTHON_EXEC = 'cd /Users/avows/Documents/coding/python_test'
    }

    stages {
    stage('Checkout') {
        steps {
            // Mengambil kode dari repositori VCS Anda
            script {
                // Pilihan 1: Git
                git branch: params.BRANCH, credentialsId: 'ddc2cf5f-8dae-4c7b-8c88-0ac92f4a0687', url: 'https://github.com/ardiansyahdiar22/POM-python.git'

            }
        }
    }

        stage('Install Dependencies') {
            steps {
                script {
                    // Instal dependencies Python menggunakan pip
                    sh "${PYTHON_EXEC} -m pip install -r requirements.txt"
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    def timestamp = new Date().format('yyyyMMddHHmmss')
                    sh "${PYTHON_EXEC} behave --format=html -o ./reports/report2.html -t ${TAG}"
                }
            }
        }
        stage('Reports Test') {
            steps {
                archiveArtifacts 'reports/report2.html'
                // def reportFiles = findFiles(glob: 'reports/report_*.html')
                // if (reportFiles) {
                //     archiveArtifacts reportFiles.collect { it.getPath() }.join(',')
                // }
            }
        }
}
    post {
        always {
            // Membersihkan lingkungan atau melakukan tindakan lain yang diperlukan setelah selesai
            cleanWs()
        }

        success {
            echo "Build Passed!"
            //archiveArtifacts 'reports/report2.html'
        }

        failure {
            // Tindakan yang harus diambil jika pipeline gagal
            echo "Build Failed!"
        }
    }
}
