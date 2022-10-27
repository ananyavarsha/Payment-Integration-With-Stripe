
# Stripe Payment Integration Using Spring Boot

A Spring Boot application that requests for a credit card from the customer to charge a specific amount. Using Stripe API, inline processing of the payment has been achieved. The application also gives the user the capabilities of managing customers. Thymeleaf is used to provide an elegant way to create and render templates.

Stripe provides a layer of abstraction that reduces the complexity of receiving payments. As a result, we don't need to deal with credit card details directly – instead, we deal with a token symbolizing an authorization to charge.

We need to have at least on payment method selected for us to create PaymentIntent API. The PaymentIntent API is the new way to build the dynamic payment flow. It tracks the lifecycle of customer checkout flow and triggers additional authentication checks.
Stripe uses a PaymentIntent object to represent your intent to collect payment from a customer, tracking charge attempts and payment state changes throughout the process.

<p align="center">
  <img src="https://user-images.githubusercontent.com/32454228/198170434-b3c70363-5785-43b8-bfe9-5be14a22f73b.png" alt="IMAGE" style="width:400px;"/>
</p>


The customer goes to the client, the client talks with stripe. Its has two pieces to it. 
* The page sends the request to the server that we build, then my server sends the request to the stripe indicating that we need to call payment_intent. The stripe accepts it and gives me a client_secret id back to the browser. When the pay button is hit, there is this second step happening. 
* With the details provided the javascript confirms the payment with client_secret id and payment details. The payment is done - it works or it doesn’t work.Then in later stages we care about web hook where we get an event back from stripe saying ‘hey!payment has happened do something act it, like unlock a video, unlock a ebook, give the user the customer account status.

### Testing APIs

* To make a payment using credit and debit cards :
          localhost:8080
this will directly take you to a customized form that you need to fill details in, on submit will take you to payment page where you enter your credentials and if on success a success message is shown.
All the payment status can be checked and verified on https://dashboard.stripe.com/test/payments

<p align="center">
  <img width="504" alt="image" src="https://user-images.githubusercontent.com/32454228/198170736-55f85e01-1d72-4794-9283-038e82f42a96.png" style="width:300px;"/>
</p>

* Adding new customer
          localhost:8080/user
this will show you list of all the existing customers and on click on new customer button, will take you to a new page where you enter the details of customer you want to add and save.
The customers added can be seen in this link : https://dashboard.stripe.com/customers

### Handle post payment events
FULFILMENT
A webhook is an endpoint on your server that receives requests from Stripe, notifying you about events that happen on your account such as a customer disputing a charge or a successful recurring payment. 
To test web hook, use stripe CLI and login, get the stripe web hook secret key using following command

stripe listen --forward-to localhost:8080/stripe/events
