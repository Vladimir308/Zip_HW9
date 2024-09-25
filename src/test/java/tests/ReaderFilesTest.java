package tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ReaderFilesTest {

    private final ClassLoader classLoader = ReaderFilesTest.class.getClassLoader();
    private Object expected;

    @Test
    public void readerPdfTest() throws Exception {
        this.expected = expected;


        ZipEntry zipEntry;
        try (ZipInputStream zipInputStream = new ZipInputStream(Objects
                .requireNonNull(classLoader.getResourceAsStream("Tests.zip")))) {
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String nameFile = zipEntry.getName();
                String expected = "Test_2.pdf";
                if (!nameFile.equals(expected))  {
                    continue;
                }
                PDF pdf = new PDF(zipInputStream);
                zipInputStream.closeEntry();
                assertThat(pdf.text).contains("Никакой полезной информации он не несёт");
            }
        }

    }

    @Test
    public void readerXlsTest() throws Exception {

        ZipEntry zipEntry;
        try (ZipInputStream zipInputStream = new ZipInputStream(Objects
                .requireNonNull(classLoader.getResourceAsStream("Tests.zip")))) {
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String nameFile = zipEntry.getName();
                String expected = "Test_3.xls";
                if (!nameFile.equals(expected)) {
                    continue;
                }

                XLS xls = new XLS(zipInputStream);
                String cellValue = xls.excel.getSheetAt(0).getRow(5).getCell(0).getStringCellValue();
                zipInputStream.closeEntry();
                assertThat(nameFile).isEqualTo("Test_3.xls", "Важный тест!", cellValue);
            }
        }
    }

    @Test
    public void readerCsvZipTest() throws Exception {

        ZipEntry zipEntry;
        try (ZipInputStream zipInputStream = new ZipInputStream(Objects
                .requireNonNull(classLoader.getResourceAsStream("Tests.zip")))) {
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String nameFile = zipEntry.getName();
                String expected = "Test_1.csv";
                if (!nameFile.equals(expected)) {
                    continue;
                }

                CSVReader reader = new CSVReader(new InputStreamReader(zipInputStream));
                List<String[]> res = reader.readAll();
                zipInputStream.closeEntry();
                assertThat(nameFile).isEqualTo("Test_1.csv",
                        new String[]{"Java", "https://habr.com"}, res.get(0),
                        new String[] {"Selenide", "https://ru.selenide.org/"}, res.get(1));
            }
        }
    }
}