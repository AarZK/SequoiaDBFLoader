package com.sequoiadb.dbfloader.prepare;

import com.github.javafaker.Faker;
import com.linuxense.javadbf.DBFDataType;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

/***
 * @Program: SequoiaDBFLoader
 * @Description: generate dbf files
 * @Author: Lzk
 * @Create: 16:14 2020/08/19
 **/
public class DBFGenerator {

    public static void generateDBF(String filePath, int fileNum, int totalRecord) {
        /**
         * @Description: generate dbf files according to some rules
         * @Param: [filePath, fileNum, totalRecord]
         * @Return: void
         * @Author: Li Zekun
         * @Create on 16:38 2020/8/19
         */
        try {
            DBFField[] fields = new DBFField[12];

            fields[0] = new DBFField();
            fields[0].setName("No");
            fields[0].setType(DBFDataType.NUMERIC);
            fields[0].setLength(10);
            fields[0].setDecimalCount(0);

            fields[1] = new DBFField();
            fields[1].setName("Pokemon");
            fields[1].setType(DBFDataType.CHARACTER);
            fields[1].setLength(30);

            fields[2] = new DBFField();
            fields[2].setName("Location");
            fields[2].setType(DBFDataType.CHARACTER);
            fields[2].setLength(30);

            fields[3] = new DBFField();
            fields[3].setName("Sex");
            fields[3].setType(DBFDataType.CHARACTER);
            fields[3].setLength(30);

            fields[4] = new DBFField();
            fields[4].setName("Birth");
            fields[4].setType(DBFDataType.DATE);

            fields[5] = new DBFField();
            fields[5].setName("Height");
            fields[5].setType(DBFDataType.NUMERIC);
            fields[5].setLength(10);
            fields[5].setDecimalCount(2);

            fields[6] = new DBFField();
            fields[6].setName("Weight");
            fields[6].setType(DBFDataType.NUMERIC);
            fields[6].setLength(10);
            fields[6].setDecimalCount(2);

            fields[7] = new DBFField();
            fields[7].setName("Length");
            fields[7].setType(DBFDataType.NUMERIC);
            fields[7].setLength(10);
            fields[7].setDecimalCount(2);

            fields[8] = new DBFField();
            fields[8].setName("Experience");
            fields[8].setType(DBFDataType.FLOATING_POINT);
            fields[8].setLength(10);
            fields[8].setDecimalCount(2);

            fields[9] = new DBFField();
            fields[9].setName("Partner");
            fields[9].setType(DBFDataType.CHARACTER);
            fields[9].setLength(30);

            fields[10] = new DBFField();
            fields[10].setName("isEvolved");
            fields[10].setType(DBFDataType.LOGICAL);

            fields[11] = new DBFField();
            fields[11].setName("RegisTime");
            fields[11].setType(DBFDataType.DATE);

            DBFWriter dbfWriter = new DBFWriter(new FileOutputStream(filePath));
            dbfWriter.setFields(fields);


            for (int i = 0; i < totalRecord; i++) {
                Faker faker = new Faker();
                Object row[] = new Object[12];
                row[0] = new BigDecimal(i);
                row[1] = faker.pokemon().name();
                row[2] = faker.pokemon().location();
                row[3] = faker.demographic().sex();
                row[4] = faker.date().birthday();
                row[5] = new BigDecimal(Math.random() * 1000).setScale(2, BigDecimal.ROUND_DOWN);
                row[6] = new BigDecimal(Math.random() * 1000).setScale(2, BigDecimal.ROUND_DOWN);
                row[7] = new BigDecimal(Math.random() * 1000).setScale(2, BigDecimal.ROUND_DOWN);
                row[8] = new BigDecimal(Math.random() * 1000).setScale(2, BigDecimal.ROUND_DOWN);
                row[9] = faker.name().fullName();
                row[10] = new Random().nextBoolean();
                row[11] = new Date();
                dbfWriter.addRecord(row);
            }
            dbfWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        generateDBF("C:\\Users\\14620\\Dropbox\\My PC (DESKTOP-HJS0QCH)\\Desktop\\pokemon.dbf", 1, 1000);
    }
}
