# munro-library-challange

API can be ran from swagger interface available after service start up locally at:
http://localhost:8080/swagger-ui.html#/

Or by using query params like this:
http://localhost:8080/munro/?totalNumberOfResults=3

Supported values include:
hillCategory - top, mun, either, case insensitive. Throws error if unexpected value provided.
maxHeightInMeters - numeric value, throws error if not numeric. 
minHeightInMeters - numeric value, throws error if not numeric. 
(if both provided, min =< max, else error)
sortByName - expected 'asc' or 'desc' throws error if unexpected value provided. 
sortByHeight - expected 'asc' or 'desc' throws error if unexpected value provided.
totalNumberOfResults - numeric, natural number expected, throws error if unexpected value.
sortingPreference - 'name' or 'height' if both values are provided, then either name or height will be sorting results in preferred order. 

no value means no filter/ sorting applies 

JUnit tests included for core functionality