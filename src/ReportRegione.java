public class ReportRegione {
    private String regione;
    private double mediaPercentuale;

    public ReportRegione(String regione, double mediaPercentuale) {
        this.regione = regione;
        this.mediaPercentuale = mediaPercentuale;
    }

    public String getRegione() {
        return regione;
    }

    public double getMediaPercentuale() {
        return mediaPercentuale;
    }
}
