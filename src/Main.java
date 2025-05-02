import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String inputFilePath = "src/Grado-diffusione-del-PC-nelle-imprese-con-meno-di-10-addetti.csv";
        String outputFilePath = "src/report.csv";
        String outputFilePathHTML = "src/report.html";

        try {
            List<DiffusionePC> dati = caricaDatiDaCSV(inputFilePath);
            List<ReportRegione> report = creaReportPerRegione(dati);
            salvaReportInCSV(report, outputFilePath);
            salvaReportInHTML(report, outputFilePathHTML);
            System.out.println("Report generato con successo in: " + outputFilePath);

            System.out.println("Report generato con successo in: " + outputFilePathHTML);
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
                writer.write(entry.getRegione() + ";" + entry.getMediaPercentuale() + "\n");
            }
        }
    }

    private static void salvaReportInHTML(List<ReportRegione> report, String filePath) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            writer.write("<!DOCTYPE html>\n");
            writer.write("<html>\n");
            writer.write("<head>\n");
            writer.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
            writer.write("<title>Report Grado-diffusione del PC</title>\n");
            writer.write("</head>\n");
            writer.write("<body style='font-family: Arial'>\n");
            writer.write("<h1>Grado-diffusione del PC</h1>\n");
            writer.write("<h2>Report per Regione</h2>\n");
            writer.write("<ul>\n");
            for (ReportRegione entry : report) {
                writer.write("<li>" + entry.getRegione() + ": " + Math.floor(entry.getMediaPercentuale()) + "%</li>\n");
            }
            writer.write("</ul>\n");
            writer.write("</body>\n");
            writer.write("</html>");
        }
    }
}
