# lab-library
An API to store and identify various items used in scientific experiments.

## Project Description
The project consists of 4 main entities name Item, Category, Attribute and ItemValue


1. Category   
    A *Category* is related to an *Attribute* in a *Many-To-Many* relationship. 
    This is so a *Category* can share some of its attributes with another *Category*. Example is an *Attribute* such as **weight** 
    can be shared with different *Categories* that has weight as an *Attribute*     
    The properties of a Category are ***id,name,description,attributes,createdDate and updatedDate***
2. Attribute    
    The properties on an Attribute are ***id,key,label,valueType,createdDate and updatedDate***  
    The *key* in this case is similar to a database column name. An example will be 
    The *label* on the hand is for display purposes. This is so the client can present a human readable attribute Attribute name
    The *valueType* is meant to hold the type of value expected by the Attribute. Example, an age can hold a value type Integer. 
    In in instances where the API need to process something based on an *Attribute*, it can rely on the valueType to better understand 
    how to deal with an item value. 
    The *createdDate* property holds the timestamp of when the *Attribute* was created
    The *updatedDate* property also holds a timestamp of when an *Attribute* was updated.
3.  Item     
    The *Item* entity has a *Many-To-One* relationship with *Category*. This is so a collection of like-items can be stored under a 
    common *Category*.
    Item has ***id,category,itemValues,createdDate and updatedDate***. 
    The *itemValues* property holds a collection of values that corresponds to the *Attributes* of the *Category* under which the *Item* is under.
    So if a *Category* named *DEVICE* has a weight *Attribute*, the *itemValue* is expected to hold the weight value of the *Attribute*.
    It does so by referencing the Attribute's ID.
    The *createdDate* property holds the timestamp of when the *itemValue* was created
    The *updatedDate* property also holds a timestamp of when an *itemValue* was updated.
4.  ItemValue      
    The *ItemValue* has a *Many-To-One* relationship with a Category's *Attribute* and an *Item*.    
    The relationship with an *Attribute* helps tell what value it is holding.
## Getting Started
Placeholder
To start. Let's first clone this repository with the below command    
> git clone https://github.com/ciriusrob/lab-library.git

Now we change directory into the cloned project
> cd lab-library

Next is to clean and package the source into a jar using the maven wrapper included
> ./mvnw clean install

The project should build successfully.
Now we can run the application using the below command
> java -jar target/api-0.0.1-SNAPSHOT.jar 

The applications should be up and running. Visit `http://localhost:9090/swagger-ui.html`


## Creating A Category With Attributes
The request body for creating a *Category* is as below
<pre>    
 {
   "name": "DEVICE",
   "description": "Device Category",
   "attributes": [
     {
       "key": "size",
       "label": "Size",
       "valueType": "String"
     },
     {
       "key": "weight",
       "label": "Weight",
       "valueType": "String"
     },
     {
       "key": "color",
       "label": "Color",
       "valueType": "String"
     }
   ]
}
</pre>

We can either use swagger or cURL to create a Category
<pre>
$ curl -X POST "http://localhost:9090/api/v1/categories"  \
   	-H "accept: application/json" \ 
   	-H "Content-Type: application/json"  \
   	-d "{ \"name\": \"DEVICE\", \"description\": \"Device Category\", \"attributes\": [ { \"key\": \"size\", \"label\": \"Size\", \"valueType\": \"String\" }, { \"key\": \"weight\", \"label\": \"Weight\", \"valueType\": \"String\" }, { \"key\": \"color\", \"label\": \"Color\", \"valueType\": \"String\" } ]}"
</pre>

## Creating An Item In A Category
Placeholder
The Item request payload looks like the JSON object below.
<pre>
{
    "name": "Test Tube",
    "categoryId": 4,
    "itemValues": [
      {
        "attributeId": 2,
        "value": "10x200 mm"
      },
      {
        "attributeId": 3,
        "value": "Transparent"
      },
      {
        "attributeId": 1,
        "value": "24.47 g"
      }
    ]
  }
</pre>
We can use swagger or cURL to create an *Item* in a *Category* 
To use cURL execute the following command from your terminal (*unix) or command prompt if you are on Windows
<pre>
curl -X POST "http://localhost:9090/api/v1/items" \ 
	-H "accept: application/json" -H "Content-Type: application/json" \ 
	-d "{ \"name\": \"Test Tube\", \"categoryId\": 4, \"itemValues\": [ { \"attributeId\": 2, \"value\": \"10x200\" }, { \"attributeId\": 3, \"value\": \"Transparent\" }, { \"attributeId\": 1, \"value\": \"24.47 g\" } ] }"
</pre>
 

## Getting Items In A Category
To get items from a particular category, we'll need to make a call to an endpoint passing the category ID
In our case the categoryId is `4`
`http://localhost:9090/api/v1/categories/{categoryId}/items`
You can add the pagination parameters to control how many items are fetch per page, page number, sorting etc
example below:

`http://localhost:9090/api/v1/categories/{categoryId}/items?page=1&pageSize=10&properties=name&direction=ASC`
<pre> 
curl -X GET "http://localhost:9090/api/v1/categories/4/items" -H "accept: application/json"
</pre>
You can also use swagger to get items under a particular category

## Updating An Item 
To update an *Item* in a Category, we'll need to call the PUT endpoint with the request payload below.
The id references the itemValue ID.
<pre>
{
  "itemValues": [
    {
      "id": 6,
      "value": "20x120 mm"
    },
    { 
      "id": 7,
      "value": "Semi Transparent"
    }
  ],
  "name": "Updated Test Tube"
}
</pre> 

To update using a cURL command
<pre>
curl -X PUT "http://localhost:9090/api/v1/items/5" \ 
	-H "accept: application/json" \ 
	-H "Content-Type: application/json" \ 
	-d "{ \"itemValues\": [ { \"id\": 6, \"value\": \"20x120 mm\" }, { \"id\": 7, \"value\": \"Semi Transparent\" } ], \"name\": \"Updated Test Tube\"}"
</pre>

Again you can use swagger to perform all these actions