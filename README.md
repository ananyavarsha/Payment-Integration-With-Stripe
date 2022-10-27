


Spring Boot
Stripe Payment Integration

Stripe is a cloud-based service that enables businesses and individuals to receive payments over the internet and offers both client-side libraries (JavaScript and native mobile) and server-side libraries (Java, Ruby, Node.js, etc.).
Stripe provides a layer of abstraction that reduces the complexity of receiving payments. As a result, we don't need to deal with credit card details directly – instead, we deal with a token symbolizing an authorization to charge.
Accepting payment in strike is of two types - Prebuilt Checkout Page(it will redirect you to the stripe specific page and redirect back to your application)  and Custom Payment Flow (https://stripe.com/docs/payments/accept-a-payment?platform=web&ui=elements). 
We need to have at least on payment method selected for us to create PaymentIntent API. The PaymentIntent API is the new way to build the dynamic payment flow. It tracks the lifecycle of customer checkout flow and triggers additional authentication checks.
Stripe uses a PaymentIntent object to represent your intent to collect payment from a customer, tracking charge attempts and payment state changes throughout the process.

The customer goes to the client, the client talks with stripe. Its has two pieces to it. The page sends the request to the server that we build, then my server sends the request to the stripe saying that ‘Hey, there is a user that intends to buy something.’So we need to create a so called payment_intent. The stripe says Okay, and gives me a client_secret id back to the browser. When the pay button is hit, there is this second step happening. With the details provided the javascript confirms the payment with client_secret id and payment details. The [payment is done - it works or it doesn’t work.Then in later stages we care about web hook where we get an event back from stripe saying ‘hey!payment has happened do something act it, like unlock a video, unlock a ebook, give the user the customer account status.
In this project everything happens inside of my app in the local server than using Stripe’s payment page. This helps me integrate a lot of other payment methods . It makes user task easy too. Instead of redirecting the user to different providers everything happens on my web page itself. 
Creating PaymentIntent
Add an endpoint on your server that creates a PaymentIntent. A PaymentIntent tracks the customer’s payment lifecycle, keeping track of any failed payment attempts and ensuring the customer is only charged once. Return the PaymentIntent’s client secret in the response to finish the payment on the client.

Configure Payment Method

With automatic_payment_methods enabled, we enable cards and other common payment methods for you by default, and you can enable or disable payment methods directly in the Stripe Dashboard. Before displaying the payment form, Stripe evaluates the currency, payment method restrictions, and other parameters to determine the list of supported payment methods. 

Handle post payment events
FULFILMENT
A webhook is an endpoint on your server that receives requests from Stripe, notifying you about events that happen on your account such as a customer disputing a charge or a successful recurring payment. Add a new endpoint to your server and make sure it’s publicly accessible so we can send unauthenticated POST requests.
To test web hook, use stripe CLI and login, get the stripe web hook secret key using following command

stripe listen --forward-to localhost:8080/stripe/events
