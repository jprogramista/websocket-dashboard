#### Development server run

Backend:
```
cd backend
mvn clean compile spring-boot:run
```
Frontend:
```
cd frontend
ng serve
```

#### REST endpoint for alerts
```
http POST :8080/alert title=Title message=message
```
You can use page form which do the same (and you can check in other browser windows, that messages are sent).

There are 2 profiles with demo setup Linux and MacOSX.

     
     