##Assumptions and General Comments

- Formatting code according Intellij profile provided.

- Some code patterns/style I used the same as in current implementation since I consider
a project standard. So for example I usually don't make parameters final (Although I understand
its goal - but used it to keep standard).

- Lombok annotations are exceptions to above decision. Using it in newer classes.

- 'delete' property it doesn't seen to be used in 'findDriver' so I decided to not use in Car entity.

- Manufacturer: I would make it embedded in Car. However the exercise explicitly says should be Entity. So I made it
 in new table with Id and createdDate just to make sure I'm not failing on requirements.

- Task 3: Not sure if Filter Pattern is related to URL Filtering or Filter Stream or Web Filters.
 So I decided to mix together URL filtering for the resource + stream filtering from all the drivers based on car
 details (findDriversByCarDetails API) .
 In terms of performance I understand would be better not query all drivers.

- Security: I understand this is a 2~3 hours exercise and I'm just passing it. So I will just enable
spring security with basic authentication. Please use any user/pass from drivers on requests.
If you expected more advanced security  you can take a look in this small project: https://bitbucket
.org/buddylabs/smart-queue/src I did or just tell me that I will be happy to implement with JWT.

- Tests: Did unit test in all layers in order to achieve at least 80% coverage. Since
many classes were given not tested I think its a good metric to this exercise.
 