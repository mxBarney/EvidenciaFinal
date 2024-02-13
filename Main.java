import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

class Usuario {
    String nombreCompleto;
    String id;
}

class Doctor extends Usuario {
    String especialidad;

    void darDeAltaDoctor(String id, Scanner scanner) {
        this.id = id;
        System.out.print("Ingrese el nombre completo del doctor: ");
        this.nombreCompleto = scanner.nextLine();
        System.out.print("Ingrese la especialidad del doctor: ");
        this.especialidad = scanner.nextLine();

        System.out.println("Doctor dado de alta con éxito.");
    }

    void guardarDoctorCSV() {
        try {
            FileWriter writer = new FileWriter("doctores.csv", true);
            writer.append(id);
            writer.append(',');
            writer.append(nombreCompleto);
            writer.append(',');
            writer.append(especialidad);
            writer.append('\n');
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Paciente extends Usuario {
    String direccion;

    void darDeAltaPaciente(String id, Scanner scanner) {
        this.id = id;
        System.out.print("Ingrese el nombre completo del paciente: ");
        this.nombreCompleto = scanner.nextLine();
        System.out.print("Ingrese la dirección del paciente: ");
        this.direccion = scanner.nextLine();
        System.out.println("Paciente dado de alta con éxito.");
    }

    void guardarPacienteCSV() {
        try {
            FileWriter writer = new FileWriter("pacientes.csv", true);
            writer.append(id);
            writer.append(',');
            writer.append(nombreCompleto);
            writer.append(',');
            writer.append(direccion);
            writer.append('\n');
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Cita {
    String idCita;
    String idPaciente;
    String idDoctor;
    String fecha;
    String hora;

    void entregarCita(String idCita, String idPaciente, String idDoctor, String fecha, String hora) {
        this.idCita = idCita;
        this.idPaciente = idPaciente;
        this.idDoctor = idDoctor;
        this.fecha = fecha;
        this.hora = hora;
        System.out.println("Cita médica entregada con éxito.");
    }


    void guardarCitaCSV() {
        try {
            FileWriter writer = new FileWriter("citas.csv", true);
            writer.append(idCita);
            writer.append(',');
            writer.append(idPaciente);
            writer.append(',');
            writer.append(idDoctor);
            writer.append(',');
            writer.append(fecha);
            writer.append(',');
            writer.append(hora);
            writer.append('\n');
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean esTraslapada(String otraCita, Cita nuevaCita) {
        String[] citaParts = otraCita.split(", ");
        String otraFecha = citaParts[0];
        String otraHora = citaParts[1];

        return otraFecha.equals(nuevaCita.fecha) && traslapoHorario(otraHora, nuevaCita.hora);
    }

    private boolean traslapoHorario(String hora1, String hora2) {
        int inicio1 = Integer.parseInt(hora1.split(":")[0]);
        int fin1 = Integer.parseInt(hora1.split(":")[1]);

        int inicio2 = Integer.parseInt(hora2.split(":")[0]);
        int fin2 = Integer.parseInt(hora2.split(":")[1]);

        // Verificar si las citas están en el mismo bloque de tiempo
        return (inicio1 < fin2 && fin1 > inicio2) || (inicio2 < fin1 && fin2 > inicio1);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Doctor> doctores = new ArrayList<>();
        List<Paciente> pacientes = new ArrayList<>();
        List<Cita> citas = new ArrayList<>();

        while (true) {
            System.out.println("\nMenú:");
            System.out.println("1. Dar de alta doctor");
            System.out.println("2. Dar de alta paciente");
            System.out.println("3. Programar cita");
            System.out.println("4. Salir");
            System.out.print("Selecciona una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    darDeAltaDoctor(doctores, scanner);
                    break;
                case 2:
                    darDeAltaPaciente(pacientes, scanner);
                    break;
                case 3:
                    programarCita(pacientes, doctores, citas, scanner);
                    break;
                case 4:
                    System.out.println("Saliendo del programa.");
                    System.exit(0);

                default:
                    System.out.println("Opción no válida. Por favor, selecciona una opción válida.");
            }
        }
    }

    private static void darDeAltaDoctor(List<Doctor> doctores, Scanner scanner) {
        Doctor doctor = new Doctor();
        System.out.print("Ingrese el ID del doctor: ");
        String id = scanner.nextLine();
        doctor.darDeAltaDoctor(id, scanner);
        doctores.add(doctor);
        doctor.guardarDoctorCSV();
    }

    private static void darDeAltaPaciente(List<Paciente> pacientes, Scanner scanner) {
        Paciente paciente = new Paciente();
        System.out.print("Ingrese el ID del paciente: ");
        String id = scanner.nextLine();
        paciente.darDeAltaPaciente(id, scanner);
        pacientes.add(paciente);
        paciente.guardarPacienteCSV();
    }

    private static void programarCita(List<Paciente> pacientes, List<Doctor> doctores, List<Cita> citas, Scanner scanner) {
        Cita cita = new Cita();

        System.out.print("Ingrese el ID de la cita: ");
        String idCita = scanner.nextLine();
        cita.idCita = idCita;

        System.out.print("Ingrese el ID del paciente: ");
        String idPaciente = scanner.nextLine();
        Paciente paciente = buscarPaciente(pacientes, idPaciente);
        if (paciente == null) {
            System.out.println("Error: Paciente no encontrado.");
            return;
        }

        System.out.print("Ingrese el ID del doctor: ");
        String idDoctor = scanner.nextLine();
        Doctor doctor = buscarDoctor(doctores, idDoctor);
        if (doctor == null) {
            System.out.println("Error: Doctor no encontrado.");
            return;
        }

        System.out.print("Ingrese la fecha de la cita (formato DD-MM-YYYY): ");
        String fecha = scanner.nextLine();
        System.out.print("Ingrese la hora de la cita (formato HH:mm): ");
        String hora = scanner.nextLine();

        cita.entregarCita(idCita, idPaciente, idDoctor, fecha, hora);
        cita.guardarCitaCSV();
    }

    private static Paciente buscarPaciente(List<Paciente> pacientes, String id) {
        for (Paciente paciente : pacientes) {
            if (paciente.id.equals(id)) {
                return paciente;
            }
        }
        return null;
    }

    private static Doctor buscarDoctor(List<Doctor> doctores, String id) {
        for (Doctor doctor : doctores) {
            if (doctor.id.equals(id)) {
                return doctor;
            }
        }
        return null;
    }

}
