package tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ReaderFilesTest {

    private final ClassLoader classLoader = ReaderFilesTest.class.getClassLoader();
    private static final ObjectMapper json = new ObjectMapper();

    @Test
    public void readerPdfTest() throws Exception {

        ZipEntry entryRead;
        try (ZipInputStream zipread = new ZipInputStream(new FileInputStream("src/test/resources/Tests.zip"));) {
            String expected = "Tests.zip/Test_2.pdf";
            while ((entryRead = zipread.getNextEntry()) != null) {
                String nameFile = entryRead.getName();
                if (!nameFile.equals(expected)) {
                    continue;
                }
                PDF pdf = new PDF(zipread);
                zipread.closeEntry();
                Assertions.assertEquals(nameFile, expected);
                Assertions.assertEquals("Милюков Владимир", pdf.author);
            }
        }

    }

    @Test
    public void readerXlsTest() throws Exception {

        ZipEntry entryRead;
        try (ZipInputStream zipread = new ZipInputStream(new FileInputStream("src/test/resources/Tests.zip"));) {
            String expected = "Tests.zip/Test_3.xls";
            while ((entryRead = zipread.getNextEntry()) != null) {
                String nameFile = entryRead.getName();
                if (!nameFile.equals(expected)) {
                    continue;
                }

                XLS xls = new XLS(zipread);
                String cellValue = xls.excel.getSheetAt(0).getRow(5).getCell(0).getStringCellValue();
                zipread.closeEntry();
                Assertions.assertEquals(nameFile, expected);
                Assertions.assertEquals("Важный тест!", cellValue);
            }
        }
    }

    @Test
    public void readerCsvZipTest() throws Exception {

        ZipEntry entryRead;
        try (ZipInputStream zipread = new ZipInputStream(new FileInputStream("src/test/resources/Tests.zip"));) {
            String expected = "Tests.zip/Test_1.csv";
            while ((entryRead = zipread.getNextEntry()) != null) {
                String nameFile = entryRead.getName();
                if (!nameFile.equals(expected)) {
                    continue;
                }

                CSVReader reader = new CSVReader(new InputStreamReader(zipread));
                List<String[]> res = reader.readAll();
                zipread.closeEntry();
                Assertions.assertEquals(nameFile, expected);
                Assertions.assertArrayEquals(new String[]{"Java", "https://habr.com"}, res.get(0));
                Assertions.assertArrayEquals(new String[]{"Selenide", "https://ru.selenide.org/"}, res.get(1));
            }
        }
    }
}