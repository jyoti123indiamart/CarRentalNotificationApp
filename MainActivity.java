public class MainActivity extends AppCompatActivity {

private DatabaseReference databaseReference;
  private String customerId; // Customer ID
  private int speedLimit = 0; // Default speed limit

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      // Fetch speed limit for the user from Firebase Realtime Database
      databaseReference = FirebaseDatabase.getInstance().getReference("SpeedLimits");

      // Get the customer ID dynamically
      customerId = getCustomerId();

      // Fetch speed limit for the customer from firebase
      fetchSpeedLimit(customerId);

      // Example: Simulate speed monitoring
      checkSpeedViolation(85); // Replace 85 with actual speed from sensors
  }

  private String getCustomerId() {
      // Replace with actual logic to fetch customer ID dynamically
      return "cust_id";
  }

  private void fetchSpeedLimit(String customerId) {
      databaseReference.get().addOnSuccessListener(snapshot -> {
        speedLimit = snapshot.getValue(Integer.class) != null ? snapshot.getValue(Integer.class) : 0;
    });
  }

  private void checkSpeedViolation(int currentSpeed) {
      if (currentSpeed > speedLimit) {
          // Notify the rental company and alert the user
          notifyRentalCompany(currentSpeed);
          alertUser(currentSpeed);
      }
  }

  private void notifyRentalCompany(int currentSpeed) {
      // Send notification to the rental company using Firebase Cloud Messaging
      String message = "A speed violation has occurred.";

      FirebaseMessaging.getInstance().send(Message.builder()
            .setToken("company_fcm_token") // Rental company FCM token
            .putData("message", message)
            .build());

  }

  private void alertUser(int currentSpeed) {
    // Send notification to the user using Firebase Cloud Messaging
    String message = "Slow down.You are exceeding the speed limit!";

    FirebaseMessaging.getInstance().send(Message.builder()
          .setToken("user_fcm_token") // User's FCM token
          .putData("message", message)
          .build());
  }
}
