# booking-api-v1
Booking API V1 to manage booking registration and search

## Swaggerhub 

openapi 3.0 definition

https://swaggerhub.com/apis/alejandro.laban/booking-api-v1/1.0.0


| Method | Endpoint                |
|--------|-------------------------|
| GET    | /consultar-reserva/{id} |
| GET    | /consultar-reserva      |
| POST   | /registrar-reserva      |

## prerequisites

* java
* maven
* Kubernetes
* Helm
* Kakfa
* Apicurio registry (avro schema registry)
* azure keyvault
* cosmosdb
* sendgrid

## How to Build
 To compile, you need to have maven and java installed
```
mvn clean install
```

## How to Run
```
mvn spring-boot:run
```

## Deploying to Kubernetes

In order to deploy to Kubernetes, you need a kubernetes cluster :
```
mvn package k8s:build k8s:resource k8s:apply
```

expose port

    kubectl port-forward service/booking-api-v1 9092:9092 -n kafka

## Kakfa

https://artifacthub.io/packages/helm/bitnami/kafka

    kubectl create namespace kafka
    kubectl config set-context --current --namespace kafka

    helm repo add bitnami https://charts.bitnami.com/bitnami
    helm repo update
    helm install kafka bitnami/kafka

expose port (only if run application from IDE)
    
    kubectl port-forward service/kafka 9092:9092 -n kafka


## Apicurio Registry
https://artifacthub.io/packages/helm/apicurio-registry-helm/apicurio-registry

    export HELM_EXPERIMENTAL_OCI=1
    helm pull oci://ghcr.io/eshepelyuk/apicurio-registry --version 1.3.0
    helm upgrade -i --wait --create-namespace -n apicurio apicurio-registry apicurio-registry-1.3.0.tgz

expose port (only if run application from IDE)
    
    kubectl port-forward service/apicurio-registry-apicurio-registry 8080:8080 -n apicurio

## CloudKarafka
steps:
* register an account on cloudkarafka
* create a instance
* retrieve username, password and brokers values

## Topics
create the following topics
* register-process
* register-process.success
* register-process.DLT

## CosmosDB
steps:
* create cosmodb
* retrieve uri and access key values

## Sendgrid
steps:

* register an account on sendgrid
* create template
* create api-key
* add valid domain and configure sender
        

## KeyVault configuration

steps:

* create keyv ault
* create an application service principal from Azure Active Directory
* give access to that service principal with read and list on keys
* create the following secrets with value their corresponding values
    * booking-register-sendgrid-apiKey (from Sendgrid)
    * azure-cosmos-key (from CosmosDB)
    * CLOUDKARAFKA-BROKERS (from cloudkarafka)
    * CLOUDKARAFKA-USERNAME (from cloudkarafka)
    * CLOUDKARAFKA-PASSWORD (from cloudkarafka)