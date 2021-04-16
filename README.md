# TasksDemo
Android Kotlin App using Clear Architecture, MVVM, unit testing and UI testing

The App has been developed using 

- Android Studio 4.1.3 
- Kotlin 1.4.32
- Coroutines 1.4.3

## Business logic
The business logic has been implemented using Clean Architecture

## Presentation
The presentation has been implemented using the MVVM architecture

## Libraries used
Here is the list of libraries used to develop the project:

- Dagger, Hilt
- kittinunf Result
- Retrofit, OkHttp and Gson
- Room

### Dagger, Hilt
Tha App uses Dagger to manage the dependency injection of use cases in the ViewModel

### kittinunf Result
Lightweight Result monad that works seamlessly with coroutines and Mockito: https://github.com/kittinunf/Result

Note: Kotlin Result type is not yet supported by Mockito as explained here https://github.com/mockito/mockito-kotlin/issues/381

### Retrofit, OkHttp and Gson
Library used to access the backend system. The Retrofit provider implemented is able to manage the staging version and the production version of the backend based on the build type. The url is defined in the app build.gradle

The OkHttp libraries logs the communication with the backend when not in 'release' mode

### Room
Room has been used to persist the data received from the remote service.

As an alternative to the database, the local data can be saved as a shared preference (just updating the dependencies in the AppModule file)  

The current implementation of Room is for demonstration purpose only: the data could be saved as json string in a single line of a single table