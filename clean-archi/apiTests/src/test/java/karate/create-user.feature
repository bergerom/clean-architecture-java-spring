Feature: Create user
  Background:
* url baseUrl
* def users = '/admin/users/'

Scenario: Create an user

Given path users
And request { userName: 'John', age: 30 }
And header Accept = 'application/json'
When method post
Then status 200
And match response == { userId: '#string' }
And print 'The value of userId is :', response['userId']