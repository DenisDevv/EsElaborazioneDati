import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String inputFilePath = "src/Grado-diffusione-del-PC-nelle-imprese-con-meno-di-10-addetti.csv";
        String outputFilePath = "src/report.csv";

        try {
            List<DiffusionePC> dati = caricaDatiDaCSV(inputFilePath);
            List<ReportRegione> report = creaReportPerRegione(dati);
            salvaReportInCSV(report, outputFilePath);

            System.out.println("Report generato con successo in: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Errore durante l'elaborazione dei dati: " + e.getMessage());
        }
    }

    private static List<DiffusionePC> caricaDatiDaCSV(String filePath) throws IOException {
        List<DiffusionePC> dati = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        for (int i = 1; i < lines.size(); i++) {
            String[] campi = lines.get(i).split(";");
            int anno = Integer.parseInt(campi[0]);
            String regione = campi[1];
            double percentuale = Double.parseDouble(campi[2].replace(",", "."));
            dati.add(new DiffusionePC(anno, regione, percentuale));
        }

        return dati;
    }

    private static List<ReportRegione> creaReportPerRegione(List<DiffusionePC> dati) {
        List<ReportRegione> report = new ArrayList<>();
        List<String> regioni = new ArrayList<>();
        for (DiffusionePC dato : dati) {
            if (!regioni.contains(dato.getRegione())) {
                regioni.add(dato.getRegione());
            }
        }

        Collections.sort(regioni);

        for (String regione : regioni) {
            double somma = 0;
            int count = 0;

            for (DiffusionePC dato : dati) {
                if (dato.getRegione().equals(regione)) {
                    somma += dato.getPercentuale();
                    count++;
                }
            }

            double media = count > 0 ? somma / count : 0;
            report.add(new ReportRegione(regione, media));
        }

        return report;
    }

    private static void salvaReportInCSV(List<ReportRegione> report, String filePath) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            writer.write("Regione;Percentuale Media\n");
            for (ReportRegione entry : report) {
                writer.write(entry.getRegione() + ";" + String.format(Locale.US, "%.2f", entry.getMediaPercentuale()) + "\n");
            }
        }
    }
}
