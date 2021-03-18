package br.edu.utfpr.biblioteca.salas.model.bo;

import br.edu.utfpr.biblioteca.salas.model.RelatorioReservas;
import br.edu.utfpr.biblioteca.salas.tools.CalendarioHelper;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

/**
 *
 * @author Leonardo Baiser <lpbaiser@gmail.com>
 */
public class ParserCsvBO {

    private String filePath;
    private List<RelatorioReservas> relatorioReservas;

    public ParserCsvBO(int tipoRelatorio, List<RelatorioReservas> relatorioReservas) throws Exception {
        String path = "";
        this.relatorioReservas = relatorioReservas;
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        switch (tipoRelatorio) {
            case 1:
                path = servletContext.getRealPath("") + "resources/relatorios/RelatorioSemanal.csv";

                filePath = new File(path).getCanonicalPath();

                writeCsvReservasSemana(relatorioReservas);
                break;
            case 2:
                path = servletContext.getRealPath("") + "resources/relatorios/RelatorioMensal.csv";

                filePath = new File(path).getCanonicalPath();

                writeCsvReservasMensalOrAnual(relatorioReservas);
                break;
            case 3:
                path = servletContext.getRealPath("") + "resources/relatorios/RelatorioAnual.csv";

                filePath = new File(path).getCanonicalPath();

                writeCsvReservasMensalOrAnual(relatorioReservas);
                break;
        }

    }

    private void writeCsvReservasSemana(List<RelatorioReservas> relatorioReservas) throws Exception {
        BufferedWriter writeFile = new BufferedWriter(new FileWriter(new File(filePath)));
        writeFile();
        for (RelatorioReservas relatorio : relatorioReservas) {
            writeFile.write(CalendarioHelper.getDiaMesAno(relatorio.getData()) + ",");
            writeFile.write(relatorio.getQuantidadeAlunosManha() + ",");
            writeFile.write(relatorio.getQuantidadeAlunosTarde() + ",");
            writeFile.write(relatorio.getQuantidadeAlunosNoite() + ",");
            writeFile.write(relatorio.getQuantidadeAlunosTotal() + ",");
            writeFile.newLine();
        }
        writeFile.close();
    }

    public void writeFile() throws IOException {
    	  BufferedWriter writeFile = new BufferedWriter(new FileWriter(new File(filePath)));
          writeFile.write("ESTATÍSTICA DO USO DAS SALAS DE ESTUDO");
          writeFile.newLine();
          writeFile.write("Mês, Manhã, Tarde, Noite, Total,");
          writeFile.newLine();
    }

    public void writeFileQtdAlunos(BufferedWriter writeFile, int quantidadeAlunosManha, int quantidadeAlunosTarde,int quantidadeAlunosNoite) throws IOException {
    	writeFile.write(quantidadeAlunosManha + ",");
        writeFile.write(quantidadeAlunosTarde + ",");
        writeFile.write(quantidadeAlunosNoite + ",");
        writeFile.write((quantidadeAlunosManha + quantidadeAlunosTarde + quantidadeAlunosNoite) + ",");
        writeFile.newLine();
    }



    private void writeCsvReservasMensalOrAnual(List<RelatorioReservas> relatorioReservas) throws Exception {
    	writeFile();
  	  	BufferedWriter writeFile = new BufferedWriter(new FileWriter(new File(filePath)));
        HashMap<String, List<RelatorioReservas>> relatorioByMes = getMeses(relatorioReservas);
        for (Map.Entry<String, List<RelatorioReservas>> meses : relatorioByMes.entrySet()) {
            writeFile.write(meses.getKey() + ",");
            int quantidadeAlunosManha = 0;
            int quantidadeAlunosTarde = 0;
            int quantidadeAlunosNoite = 0;
            for (RelatorioReservas relatorio : meses.getValue()) {
            	relatorio.incrementarAlunos(quantidadeAlunosManha, quantidadeAlunosTarde, quantidadeAlunosNoite);
            }
            writeFileQtdAlunos(writeFile, quantidadeAlunosManha, quantidadeAlunosTarde, quantidadeAlunosNoite);

        }
        writeFile.close();
    }

    private HashMap<String, List<RelatorioReservas>> getMeses(List<RelatorioReservas> relatorioReservas) {
        HashMap<String, List<RelatorioReservas>> relatorioByMes = new HashMap<>();
        for (RelatorioReservas relatorio : relatorioReservas) {
            String mes = CalendarioHelper.getNomeMes(relatorio.getData());
            if (!relatorioByMes.containsKey(mes)) {
                relatorioByMes.put(mes, new ArrayList<RelatorioReservas>());
                relatorioByMes.get(mes).add(relatorio);
            } else {
                relatorioByMes.get(mes).add(relatorio);
            }

        }
        return relatorioByMes;
    }

}
