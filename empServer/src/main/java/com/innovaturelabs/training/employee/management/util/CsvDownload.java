package com.innovaturelabs.training.employee.management.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.innovaturelabs.training.employee.management.exception.BadRequestException;

public class CsvDownload {

    private CsvDownload() {
    }

    public static void download(HttpServletResponse httpServletResponse, Collection<?> exportlist, String fileName) {
        
        Collection<String> csvHeader = new ArrayList<>();
        Collection<String> nameMapping = new ArrayList<>();

        for (Field field : exportlist.iterator().next().getClass().getDeclaredFields()) {
            csvHeader.add(field.getName());
            nameMapping.add(field.getName());
        }

        download(httpServletResponse, exportlist, fileName, csvHeader, nameMapping);

    }

    public static void download(HttpServletResponse httpServletResponse, Collection<?> exportlist, String fileName,
            Collection<String> csvHeader, Collection<String> nameMapping) {

        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        String headerKey = "Content-Disposition";
        String headerValue = fileName + sdf.format(dt) + ".csv";
        httpServletResponse.setHeader(headerKey, headerValue);
        httpServletResponse.setContentType("text/csv;");
        httpServletResponse.setCharacterEncoding("shift-jis");
        httpServletResponse.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

        try {

            ICsvBeanWriter csvWriter = new CsvBeanWriter(httpServletResponse.getWriter(),
                    CsvPreference.STANDARD_PREFERENCE);

            csvWriter.writeHeader(csvHeader.toArray(String[]::new));

            for (Object reservation : exportlist) {
                csvWriter.write(reservation, nameMapping.toArray(String[]::new));
            }

            csvWriter.close();
        } catch (IOException e) {
            throw new BadRequestException("Exception while exporting csv");
        }
    }

}
