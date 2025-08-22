package com.sisol.sys_citas;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@SpringBootApplication
public class SysCitasApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SysCitasApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Imprimir todos los datos de la tabla medico para corroborar la conexión
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/sisolv_final", "root", "1234");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM medico")) {

            java.sql.ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            System.out.println("Datos de la tabla medico:");
            // Imprimir encabezados
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + "\t");
            }
            System.out.println();
            // Imprimir filas
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rs.getString(i) + "\t");
                }
                System.out.println();
            }
        } catch (Exception e) {
            System.err.println("Error al consultar la tabla medico en la base de datos: " + e.getMessage());
        }
    }
}