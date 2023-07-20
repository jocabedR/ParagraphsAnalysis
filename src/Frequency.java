import java.util.Dictionary;

// Use esta clase para guardar el total de palabras de un texto
// y un diccionario que contiene cad palabra en el texto con su frecuencia
public class Frequency{
    private Dictionary<String, Integer> wordFrequency;
    private int totalWords;

    public Frequency(Dictionary<String, Integer> wordFrequency, int totalWords) {
        this.wordFrequency = wordFrequency;
        this.totalWords = totalWords;
    }

    public Dictionary<String, Integer>  getWordFrequency() {
        return wordFrequency;
    }

    public int getTotalWords() {
        return totalWords;
    }
}