/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

import jakarta.transaction.Transactional;

/**
 * PetClinic Spring Boot Application.
 *
 * @author Dave Syer
 *
 */
@SpringBootApplication
@ImportRuntimeHints(PetClinicRuntimeHints.class)
public class PetClinicApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(PetClinicApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Hello World");

		Connection c = null;
		int resp;
		int ctrl = 0;
		try {
			c = (Connection) DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/petclinic", "petclinic",
					"petclinic");
			System.out.println("Connection created");

			String sql = "SELECT * FROM owners";
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			System.out.printf("|%10s|%10s|%10s|%25s|%15s|%15s|%n", "ID", "Nombre", "Apellido", "Direccion", "Ciudad",
					"Telefono");
			while (rs.next()) {

				System.out.printf("|%10d|%10s|%10s|%25s|%15s|%15s|%n", rs.getInt("id"), rs.getString("first_name"),
						rs.getString("last_name"), rs.getString("address"), rs.getString("city"),
						rs.getString("telephone"));
				// printf d para el int y s para los string, %numero de espacios
			}
			rs.close();
			stmt.close();

			Scanner teclado = new Scanner(System.in);

			while (ctrl == 0) {
				System.out.println("1. Insertar un nuevo cliente");
				System.out.println("2. Borrar usuario");
				System.out.println("3. Modificar ciudad");
				System.out.println();
				System.out.println("4. Mostrar todo");
				System.out.println("5. Salir");

				resp = Integer.parseInt(teclado.nextLine());

				if (resp == 1) {

					System.out.println("ID: ");
					String id = teclado.nextLine();

					System.out.println("Nombre: ");
					String nombre = teclado.nextLine();

					System.out.println("Apellido: ");
					String apellido = teclado.nextLine();

					System.out.println("Direccion: ");
					String dir = teclado.nextLine();

					System.out.println("Ciudad: ");
					String ciudad = teclado.nextLine();

					System.out.println("Teléfono: ");
					String tlf = teclado.nextLine();

					String query = "INSERT INTO owners VALUES (?,?,?,?,?,?)";
					PreparedStatement pst = c.prepareStatement(query);
					pst.setInt(1, Integer.parseInt(id));
					pst.setString(2, nombre);
					pst.setString(3, apellido);
					pst.setString(4, dir);
					pst.setString(5, ciudad);
					pst.setString(6, tlf);

					pst.executeUpdate();
					pst.close();
					ctrl = 1;
				}
				else if (resp == 2) {

					System.out.println("ID del cliente a eliminar:");
					int idborrar = Integer.parseInt(teclado.nextLine());
					String updt = "SET FOREIGN_KEY_CHECKS=0";
					Statement stmt2 = c.createStatement();
					stmt2.executeUpdate(updt);
					String borrar = "DELETE FROM owners WHERE id = ?";
					PreparedStatement pst2 = c.prepareStatement(borrar);
					pst2.setInt(1, idborrar);
					pst2.executeUpdate();
					pst2.close();
					System.out.println("Eliminado");

				}
				else if (resp == 3) {
					System.out.println("Nombre de la nueva ciudad: ");
					String ciudadN = teclado.nextLine();
					System.out.println("Id del usuario a modificar: ");
					int idU = Integer.parseInt(teclado.nextLine());
					String cambioCiudad = "UPDATE owners SET city = ? WHERE id = ?";
					PreparedStatement pst2 = c.prepareStatement(cambioCiudad);
					pst2.setString(1, ciudadN);
					pst2.setInt(2, idU);
					pst2.executeUpdate();
					pst2.close();
					System.out.println("Cambio realizado con éxito");
				}
				else if (resp == 4) {

					sql = "SELECT * FROM owners";
					Statement stmt3 = c.createStatement();
					ResultSet rs3 = stmt3.executeQuery(sql);
					System.out.printf("|%10s|%10s|%10s|%25s|%15s|%15s|%n", "ID", "Nombre", "Apellido", "Direccion",
							"Ciudad", "Telefono");
					while (rs3.next()) {

						System.out.printf("|%10d|%10s|%10s|%25s|%15s|%15s|%n", rs3.getInt("id"),
								rs3.getString("first_name"), rs3.getString("last_name"), rs3.getString("address"),
								rs3.getString("city"), rs3.getString("telephone"));
						// printf d para el int y s para los string, %numero de espacios
					}
					rs3.close();
				}
				else if (resp == 5) {
					//PREGUNTAR POR TALLER 4 (NO SE QUE HAY QUE HACERLE AHI)
					ctrl = 1;
				}

			}
		}

		catch (SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}

	}

}
