import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

public class Analysis {
    
    private String content;
    private float percentage;
    private String lookingWord;
    private int newLength;

    private Dictionary<String, Integer> wordsContent  = new Hashtable<>();
    private List<Frequency> paragraphsInfo = new ArrayList<>();
    private int totalContentWords = 0;
    private List<WordPercentage> wordsPercentage = new ArrayList<>();


    public Analysis(String content, float percentage, String lookingWord, int newLength){
        this.content = content;
        this.percentage = percentage;
        this.lookingWord = lookingWord;
        this.newLength = newLength;

        cleanContent();
        
        // Para inicializar la información del contenido y de cada párrafo.
        setInformation();
    }

    public void startAnalysis(){
        Collections.sort(this.wordsPercentage, new PercentageComparator());

        for (WordPercentage wordPercentage : wordsPercentage) {
            //System.out.println(wordPercentage.getWord() + ": " + wordPercentage.getPercentage());

            String word = wordPercentage.getWord();
            float contentPercentage = wordPercentage.getPercentage();

            // Continua con la siguiente iteración por el porcentaje que representa la palabra es menor al pocentaje que introdujo el usuario.
            if (contentPercentage < this.percentage) continue;

            // El uso de Pattern y Matcher para la aplicación de Expresiones regulares fue consultado de la documentación oficial de Java:
            // https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html
            Pattern p = Pattern.compile("\\w*?"+this.lookingWord+"\\w*?");
            Matcher m = p.matcher(word);

            if(m.matches()) System.out.println("Palabra: *" + word +"\tfrecuencia: "+ wordsContent.get(word) +"/"+ contentPercentage +"%");
            else System.out.println("Palabra: " + word +"\tfrecuencia: "+ wordsContent.get(word) +"/"+ contentPercentage +"%");

            int paragraphCounter = 1;
            for (Frequency diccionario : paragraphsInfo) {
                float paragraphPercentage = 0.0f;
                int frequency = 0;

                if(diccionario.getWordFrequency().get(word) != null) {
                    paragraphPercentage = (float)diccionario.getWordFrequency().get(word)/diccionario.getTotalWords() * 100;
                    frequency = diccionario.getWordFrequency().get(word);
                }

                System.out.println("\tPárrafo "+paragraphCounter+": "+ frequency +"/"+paragraphPercentage+"%");
                paragraphCounter++;
            }

            System.out.println("\n");
            
        }  

        System.out.println(createParagraph());

    }

    private Frequency wordFrequency(String text){
        // Diccionario para almacenar cada palabra y su frecuencia
        Dictionary<String, Integer> wordsCounter = new Hashtable<>();

        // Expresión regular para identificar las palabras(inclyendo las que contienen un guión o un apostrofe), puntos y signos de interrogación.
        Pattern p = Pattern.compile("(\\b\\w+\\b(-\\w+|'\\w+)|\\b\\w+\\b|[\\.\\?])");
        Matcher m = p.matcher(text);
        
        // Alamacena el total de palabras del texto que se analiza, puede ser de todo el contenido o de un párrafo
        int totalWords = 0;
        while (m.find()) {
            ++totalWords;
            String key = m.group();

            // Cada que se encuentra una coincidencia se incrementa la frecuencia. 
            try {
                int frequency = wordsCounter.get(key);
                wordsCounter.remove(key);
                wordsCounter.put(key, ++frequency);
            } catch(Exception e) { // En caso contrario de almacena la palabra y se inicializa su frecuencia en 1.
                wordsCounter.put(key, 1);
            }
        }

        // Se retorna un objeto de tipo Frequency que contiene el total de palabras del texto y un Diccionario
        // En el que se almacenó cada palabra con su respectiva frecuencia.
        return new Frequency(wordsCounter, totalWords);
    }

    private void setInformation() {
        // Para obtener la información de todo el texto
        Frequency contentInfo = wordFrequency(this.content);
        wordsContent = contentInfo.getWordFrequency();
        totalContentWords = contentInfo.getTotalWords();


        // Para obtener los párrafos del texto.
        String[] paragraphs = content.split("\\r\\n\\r\\n");

        for(int i=0; i<paragraphs.length; i++) {
            String paragraph = paragraphs[i].trim();

            Frequency paragraphInfo = wordFrequency(paragraph);
            
            // Se almacena la información del párrafo.
            paragraphsInfo.add(paragraphInfo);
        }

        // Me pareció que almacenar la cada palabra y su porcentaje de frecuencia en una lista,
        // sería una buena opción para posteriormente ordenar por el porcentage.
        // Este pedazo de codigo, que se usa para sacar cada elemento de un Diccionario lo obtuve de:
        // https://www.geeksforgeeks.org/java-util-dictionary-class-java/
        Enumeration<String> k = wordsContent.keys();
        while (k.hasMoreElements()) {
            String word = k.nextElement();
            float contentPercentage = (float) wordsContent.get(word) / totalContentWords * 100;
            
            wordsPercentage.add(new WordPercentage(word, contentPercentage));
        } 
    }


    private void cleanContent(){
        // Eliminar comentarios
        this.content = this.content.replaceAll("(?m)<<[\\s\\S]*>>", "");
        //this.content = this.content.replaceAll("^[^\\s{2,4}].+$", "");

        // Pasar todo a mayúculas
        this.content = this.content.toUpperCase();

        //Separar la puntuación (s, “ ,  .  :  ;  !  ?  ‘s ”)
        // Se consultó: https://docs.oracle.com/javase/8/docs/api/java/lang/String.html#replaceAll-java.lang.String-java.lang.String-
        this.content = this.content.replaceAll("(\\u2018|\\u0060|\\u2019)", "'");
        this.content = this.content.replaceAll("(\\b\'S)", "");
        this.content = this.content.replaceAll("S\\b", "");
        this.content = this.content.replaceAll(",", " , ");
        this.content = this.content.replaceAll("\\.", " . ");
        this.content = this.content.replaceAll(":", " : ");
        this.content = this.content.replaceAll(";", " ; ");
        this.content = this.content.replaceAll("!", " ! ");
        this.content = this.content.replaceAll("\\?", " ? ");
        this.content = this.content.replaceAll("[“”\"]", " \" ");
    }


    public String createParagraph() {
        //Obtener una palabra ramdom
        Random random = new Random();
        String paragraph, word;
        int index;

        // En caso de que la palabra que insertó el usuario no se encuentre el texto       
        if(wordsContent.get(this.lookingWord) == null){
            index = random.nextInt(this.wordsContent.size()/4);
            word = this.wordsPercentage.get(index).getWord();
        }
        else word = this.lookingWord;
        paragraph = word;
        
        // Buscar las posibles siguientes palabras
        List<String> possibleNext = new ArrayList<>();
        for(int i=2; i<=this.newLength; i++){
            Pattern p = Pattern.compile("\\b"+ word +"\\b\\s"+"(\\b\\w+\\b(-\\w+|'\\w+)|\\b\\w+\\b|[\\.\\?])");
            Matcher m = p.matcher(this.content);

            while (m.find()) {
                String next = m.group();
                next = next.replaceAll("\\b"+ word +"\\b\\s", "");
                if (next != word) possibleNext.add(next.trim());
            }

            // Si no se encontraron palabras para continuar el parrafo se usará para delimitar la frase
            word = ".";
            // Escogiendo una palabra aleatoria de las palabras que le pueden suceder.
            if (possibleNext.size() > 0 ) {
                index = random.nextInt(possibleNext.size());
                word = possibleNext.get(index);
            }
            
            paragraph = paragraph + "\s" +word;
            possibleNext = new ArrayList<>();
        }
        
        return paragraph;
    }
    
}
