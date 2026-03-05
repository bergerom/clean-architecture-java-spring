Feature: Create session
  Background:
    * url baseUrl
    * def sessionSecure = '/secure/session'
    * def userId = call read('create-user.feature')

Given path sessionSecure
And request { }
And header Accept = 'application/json'
When method post
Then status 201
And match response == { sessionId: '#string' }
* def sessionId = response.sessionId
* print sessionId