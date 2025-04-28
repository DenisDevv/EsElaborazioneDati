public class DiffusionePC {
    private int anno;
    private String regione;
    private double percentuale;

    public DiffusionePC(int anno, String regione, double percentuale) {
        this.anno = anno;
        this.regione = regione;
        this.percentuale = percentuale;
    }

    public int getAnno() {
        return anno;
    }

    public String getRegione() {
        return regione;
    }

    public double getPercentuale() {
        return percentuale;
    }
}
