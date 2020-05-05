# Estimator created for Landscaping Industry
<h2> Language Used </h2>

<p> The entire app is programmed with <b>Kotlin</b> except for <i>TypeConverter</i> where <b>Java</b> is being used</p>

<h2> Function of The App </h2>

The app facilitates workers in landsacping industry by offering them a user-friendly interface where they can create easily an estimation for each customer. <br>

For each customer, the worker can enter 6 different services items by selecting the correspondent service name, and enter the
correspondent dimensions. The app will calculate automatically the size and the cost needed for each service (By using formulae implemented in the app). At the end, the app will also generate the total cost of each estimation. The woker can choose to save the estimation and check it for references

In summary, the app saves a lot of time for people who work in Landscaping Industry because they don't need to use a pen and a 
piece of paper to record estimation when they are with customers 

<h2> Key Components </h2>
<ul>
  <li> Firebase Authentification --> Handle Authentification of Users </li>
  <li> Firebase Storage --> In corporating with Firebase Authentification to save estimation data seperated by User UID.</li>
  <li> Room Database --> Android SQL Database to handle the saving of estimation to local (Later sent to Firebase Storage)
  <li> Gson --> Use Gson to convert customized data class to JSON file in order to upload them to Firebase Storage (Also serves to convert JSON back to data class in order to manipulate in the app) </li>
</ul>

<b><i>Created by Yan Zhuang in April, 2020.</b></i>
