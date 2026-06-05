import java.util.*;

class FileNotSupportedException extends Exception{
    public FileNotSupportedException(String message){
        super(message);
    }
}

class AttachmentsSizeExceededException extends Exception{
    public AttachmentsSizeExceededException(String message){
        super(message);
    }
}

class LLMModelDetails{
    private String modelName;
    private double inputTokenPrice;
    private double outputTokenPrice;
    private double mbAttachmentPrice;

    public LLMModelDetails(String modelName, double inputTokenPrice, double outputTokenPrice, double mbAttachmentPrice) {
        this.modelName = modelName;
        this.inputTokenPrice = inputTokenPrice;
        this.outputTokenPrice = outputTokenPrice;
        this.mbAttachmentPrice = mbAttachmentPrice;
    }

    public String getModelName() {
        return modelName;
    }

    public double getInputTokenPrice() {
        return inputTokenPrice;
    }

    public double getOutputTokenPrice() {
        return outputTokenPrice;
    }

    public double getMbAttachmentPrice() {
        return mbAttachmentPrice;
    }
}

class AttachMent{
    String fileName;
    int fileSizeInMb;

    public AttachMent(String fileName, int fileSizeInMb) {
        this.fileName = fileName;
        this.fileSizeInMb = fileSizeInMb;
    }

    public String getFileName() {
        return fileName;
    }

    public int getFileSizeInMb() {
        return fileSizeInMb;
    }
}

class Interaction{
    private String userId;
    private String sessionId;
    private String question;
    private long timestampQuestion;
    private String answer;
    private long timestampAnswer;
    List<AttachMent> attachments;
    LLMModelDetails modelDetails;

    public Interaction(String userId, String sessionId, String question, long timestampQuestion, String answer, long timestampAnswer, List<AttachMent> attachments,LLMModelDetails modelDetails) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.question = question;
        this.timestampQuestion = timestampQuestion;
        this.answer = answer;
        this.timestampAnswer = timestampAnswer;
        this.attachments = attachments;
        this.modelDetails = modelDetails;
    }

    public String getUserId() {
        return userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getQuestion() {
        return question;
    }

    public long getTimestampQuestion() {
        return timestampQuestion;
    }

    public String getAnswer() {
        return answer;
    }

    public long getTimestampAnswer() {
        return timestampAnswer;
    }

    public List<AttachMent> getAttachments() {
        return attachments;
    }

    public LLMModelDetails getModelDetails() {
        return modelDetails;
    }

    public long processingTime(){
        return timestampAnswer - timestampQuestion;
    }

    public double inputTokens(){
        return Math.ceil(question.length() / 4.0);
    }

    public double outputTokens(){
        return Math.ceil(answer.length() / 4.0);
    }

    public int totalAttachmentsMb(){
        return attachments.stream()
                .mapToInt(AttachMent::getFileSizeInMb)
                .sum();
    }

    public double price(){
        return inputTokens() * modelDetails.getInputTokenPrice() + outputTokens() * modelDetails.getOutputTokenPrice() + totalAttachmentsMb() * modelDetails.getMbAttachmentPrice();
    }

    @Override
    public String toString(){
        return String.format("Q: %s\nAttachments: %d\nA: %s\nProcessing time: %d Price: %.2f", question, attachments.size(), answer, processingTime(), price());
    }
}

class Session{
    String sessionID;
    Set<Interaction> interactions;

    public Session(String sessionID) {
        this.sessionID = sessionID;
        this.interactions = new TreeSet<>(Comparator.comparingLong(Interaction::getTimestampQuestion).thenComparingLong(Interaction::getTimestampAnswer));
    }

    public void addInteraction(Interaction interaction){
        interactions.add(interaction);
    }

    public void printInteractions(){
        interactions.forEach(System.out::println);
    }

    public double totalInputTokens(){
        return interactions.stream()
                .mapToDouble(Interaction::inputTokens)
                .sum();
    }

    public double totalOutputTokens(){
        return interactions.stream()
                .mapToDouble(Interaction::outputTokens)
                .sum();
    }

    public double totalPrice(){
        return interactions.stream()
                .mapToDouble(Interaction::price)
                .sum();
    }

    public double totalProcessingTime(){
        return interactions.stream()
                .mapToDouble(Interaction::processingTime)
                .sum();
    }

    public int totalAttachmentsCount(){
        return interactions.stream()
                .mapToInt(i -> i.getAttachments().size())
                .sum();
    }

    public int totalAttachmentsMb(){
        return interactions.stream()
                .mapToInt(Interaction::totalAttachmentsMb)
                .sum();
    }

    public void printDetails(){
        System.out.println("Session ID: " + sessionID);
        System.out.println("Interactions: " + interactions.size());
        System.out.println(String.format("Input tokens: %.0f", totalInputTokens()));
        System.out.println(String.format("Output tokens: %.0f", totalOutputTokens()));
        System.out.println(String.format("Price: %.2f", totalPrice()));
        System.out.println(String.format("Processing time: %d", totalProcessingTime()));
        System.out.println(String.format("Number of attachments: %d", totalAttachmentsCount()));
        System.out.println(String.format("Total attachment size: %d", totalAttachmentsMb()));
    }



}


class User{
    private String userId;
    private Map<String,Session> sessions;

    public User(String userId){
        this.userId = userId;
        this.sessions = new HashMap<>();
    }

    public void addInteraction(String sessionId, String question, long timestampQuestion, String answer, long timestampAnswer, List<AttachMent> attachMents,LLMModelDetails llmModelDetails){
        sessions.putIfAbsent(sessionId, new Session(sessionId));

        Session s = sessions.get(sessionId);

        s.addInteraction(new Interaction(userId,sessionId,question,timestampQuestion,answer,timestampAnswer,attachMents,llmModelDetails));
    }


    public void printDetails() {
        sessions.values()
                .stream()
                .sorted(Comparator.comparing(Session::totalAttachmentsCount).thenComparing(Session::totalPrice).reversed())
                .forEach(s -> s.printDetails());
    }




}




class ChatBot{
    private LLMModelDetails llmModelDetails;
    private List<String> notSupportedFiles;
    private int allowedAttachmentsSize;
    Map<String,User> users;


    public ChatBot(LLMModelDetails llmModelDetails, List<String> notSupportedFiles, int allowedAttachmentsSize) {
        this.llmModelDetails = llmModelDetails;
        this.notSupportedFiles = notSupportedFiles;
        this.allowedAttachmentsSize = allowedAttachmentsSize;
        this.users = new TreeMap<>();
    }

    public void addInteraction(String userId, String sessionId, String question, long timestampQuestion,String answer,long timestampAnswer,List<AttachMent> attachments){
        Interaction interaction = new Interaction(userId,sessionId,question,timestampQuestion,answer,timestampAnswer,attachments,llmModelDetails);
        if ()
    }
}


public class ChatbotTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Read LLMModelDetails properties
        String modelName = scanner.next();
        double inputTokenPrice = scanner.nextDouble();
        double outputTokenPrice = scanner.nextDouble();
        double mbAttachmentPrice = scanner.nextDouble();

        LLMModelDetails llmModelDetails = new LLMModelDetails(modelName, inputTokenPrice, outputTokenPrice, mbAttachmentPrice);

        // Read list of notSupportedFiles
        scanner.nextLine(); // Consume newline
        List<String> notSupportedFiles = Arrays.asList(scanner.nextLine().split(";"));

        // Read allowedAttachmentsSize
        int allowedAttachmentsSize = scanner.nextInt();
        scanner.nextLine();

        Chatbot chatbot = new Chatbot(llmModelDetails, notSupportedFiles, allowedAttachmentsSize);

        while (scanner.hasNext()) {
            String[] parts = scanner.nextLine().split(";");
            String command = parts[0];

            switch (command) {
                case "addInteraction": {
                    try {
                        String userId = parts[1];
                        String sessionId = parts[2];
                        String question = parts[3];
                        long timestampQuestion = Long.parseLong(parts[4]);
                        String answer = parts[5];
                        long timestampAnswer = Long.parseLong(parts[6]);
                        int attachmentCount = Integer.parseInt(parts[7]);
                        List<Attachment> attachments = new ArrayList<>();

                        for (int i = 0; i < attachmentCount; i++) {
                            String fileName = parts[8 + i * 2];
                            int fileSize = Integer.parseInt(parts[9 + i * 2]);
                            attachments.add(new Attachment(fileName, fileSize));
                        }

                        chatbot.addInteraction(userId, sessionId, question, timestampQuestion, answer, timestampAnswer, attachments);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case "printConversation": {
                    String userId = parts[1];
                    String sessionId = parts[2];
                    chatbot.printConversation(userId, sessionId);
                    break;
                }
                case "printSessionDetails": {
                    String userId = parts[1];
                    String sessionId = parts[2];
                    chatbot.printSessionDetails(userId, sessionId);
                    break;
                }
                case "printUserDetails": {
                    String userId = parts[1];
                    chatbot.printUserDetails(userId);
                    break;
                }
                case "longestProcessingTimeInteractions": {
                    chatbot.longestProcessingTimeInteractions();
                    break;
                }
                case "mostExpensiveInteractions": {
                    chatbot.mostExpensiveInteractions();
                    break;
                }
                case "exit": {
                    return;
                }
            }
        }

        scanner.close();
    }
}