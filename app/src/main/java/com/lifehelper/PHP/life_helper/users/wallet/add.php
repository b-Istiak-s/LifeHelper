<?php

require_once '../../connection.php';
if ( $_SERVER['REQUEST_METHOD'] == 'POST' ){
    require_once('./stripe-php/init.php');
    $username = mysqli_real_escape_string($conn,stripcslashes($_POST['username']));
    $amount = (int) mysqli_real_escape_string($conn,stripcslashes($_POST['amount']));
    $cartContent = mysqli_real_escape_string($conn,stripcslashes($_POST['items']));
    $password = mysqli_real_escape_string($conn,stripcslashes($_POST['password']));

    // This is a public sample test API key.
    // Don’t submit any personally identifiable information in requests made with this key.
    // Sign in to see your own test API key embedded in code samples.
    \Stripe\Stripe::setApiKey('sk_test_4eC39HqLyjWDarjtT1zdp7dc');

    // function calculateOrderAmount(array $items): int {
        // Replace this constant with a calculation of the order's amount
        // Calculate the order total on the server to prevent
        // people from directly manipulating the amount on the client
        // return 150000;
    // }


    try {
        // retrieve JSON from POST body
        // $jsonStr = file_get_contents('php://input');
        // $jsonObj = json_decode($jsonStr);

        // Create a PaymentIntent with amount and currency
        $paymentIntent = \Stripe\PaymentIntent::create([
            'amount' => $amount,
            'currency' => 'usd',
            'automatic_payment_methods' => [
                'enabled' => true,
            ],
        ]);

        $output = [
            'clientSecret' => $paymentIntent->client_secret,
        ];

        echo json_encode($output);
    } catch (Error $e) {
        http_response_code(500);
        echo json_encode(['error' => $e->getMessage()]);
    }
}
 mysqli_close($conn);
?>
