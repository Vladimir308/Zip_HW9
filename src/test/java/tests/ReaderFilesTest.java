package tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
                if (!nameFile.equals(expected)) {
                    continue;
                }
                PDF pdf = new PDF(zipInputStream);
                zipInputStream.closeEntry();
                Assertions.assertEquals(nameFile, expected);
                Assertions.assertEquals("Милюков Владимир", pdf.author);
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
                if (!nameFile.equals(expected)) {
                    continue;
                }

                XLS xls = new XLS(zipInputStream);
                String cellValue = xls.excel.getSheetAt(0).getRow(5).getCell(0).getStringCellValue();
                zipInputStream.closeEntry();
                Assertions.assertEquals(nameFile, expected);
                Assertions.assertEquals("Важный тест!", cellValue);
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
                if (!nameFile.equals(expected)) {
                    continue;
                }

                CSVReader reader = new CSVReader(new InputStreamReader(zipInputStream));
                List<String[]> res = reader.readAll();
                zipInputStream.closeEntry();
                Assertions.assertEquals(nameFile, expected);
                Assertions.assertArrayEquals(new String[]{"Java", "https://habr.com"}, res.get(0));
                Assertions.assertArrayEquals(new String[]{"Selenide", "https://ru.selenide.org/"}, res.get(1));
            }
        }
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }
}