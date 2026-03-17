Feature: Create user
  Background:
* url baseUrl
* def users = '/users/'

Scenario: Get all users

Given path users
And request { userName: 'John', age: 30 }
And header Accept = 'application/json'
When method get
Then status 200
# And match response == { userId: '#string' }
And print 'response', response