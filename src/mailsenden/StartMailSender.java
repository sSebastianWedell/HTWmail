package mailsenden;

public class StartMailSender {
	public static void main(String[] args) {
		
		//---------------------------------->
		// bootstrap the client
		ExternalTaskClient client = ExternalTaskClient.create()
		// API
		  //.baseUrl("http://localhost:8080/engine-rest")
		  .baseUrl("http://camunda.f4.htw-berlin.de:8080")	
		  .build();

		// subscribe to the topic
		client.subscribe("creditScoreChecker")
		  .lockDuration(1000)
		  .handler((externalTask, externalTaskService) -> {
			  
			// retrieve a variable from the Process Engine
			    int defaultScore = externalTask.getVariable(firstname, lastname, mail, subtitle);

			    List<Integer> creditScores = new ArrayList<>(Arrays.asList(defaultScore, 9, 1, 4, 10));

		    // create an object typed variable
		    ObjectValue creditScoresObject = Variables
		      .objectValue(creditScores)
		      .create();
		  //<----------------------------------
		
			String username = "htw.berlin.bpmn@gmail.com";
			String password = "P@ssw0rd5!=";
			
		    //String firstname = "Sebastian";
		    //String lastname = "Wedell";
		    //String mail = "s0572876@htw-berlin.de";
		    //String subtitle = "Pizza ist in 3 min da";
			
			
			MailSender sender = new MailSender();
			sender.login("smtp.gmail.com", "465", username, password);
	
			try {
	
				sender.send("htw.berlin.bpmn@gmail.com", firstname + lastname, mail, subtitle,
						"Hallo "+ firstname +",\r\n\r\n"
						+ "die Pizza ist gleich vor Deiner Haustür! =)\r\n"
						+ "Gern kannst Du Dich mit Deiner Mailadresse ("+mail+") für unseren Newsletter eintragen um \r\nleckere Angebote zu erhalten.\r\n\r\n"
						+ "Schöne Grüße und guten Appetit,\r\n\r\n"
						+ "Dein Pizza-Team");
	
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//---------------------------------->
			// complete the external task
		    externalTaskService.complete(externalTask,
		      Collections.singletonMap("creditScores", creditScoresObject));

		    System.out.println("The External Task " + externalTask.getId() + " has been completed!");
		}).open();
		//<----------------------------------
		
	}
}