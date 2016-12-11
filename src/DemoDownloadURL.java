import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

public class DemoDownloadURL {

	public static void main(String[] args) throws IOException {
		
		String resource = "";	
		BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
		URL url = null;
		//Imagen de ejemplo		
		
		// 1. pedimos una URL por linea de comandos
		System.out.println("Introduzca la URL del recurso");
		//Introducimos las url por linea de comandos
		resource = args[0];		//http://www.muylinux.com/wp-content/uploads/2016/03/ubuntu-16.04.jpg
		
		if(resource.equals("")){//Si no se introduce url
			System.out.println("No introdujo la URL del recurso a descargar...");
		}else{
			// 2. creamos el objeto URL
			try{
				url = new URL(resource);
			}catch(MalformedURLException e){
				e.printStackTrace();
			}
				
			// 3. Obtenemos un objeto HttpURLConnection. openConnection 
			HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
					
			// 4. configuramos la conexión al método GET. setRequestMethod
			conexion.setRequestMethod("GET");
			
			// 5. Nos conectamos. connect
			conexion.connect();
			
			// 6. Obtenemos y imprimimos el código de respuesta. getResponseCode
			System.out.println("Código de respuesta : "+conexion.getResponseCode()+" "+conexion.getResponseMessage());
			
			//Comprobamos que el recurso es una imagen
			if(conexion.getContentType().startsWith("image/")){
				
				// 7. Obtenemos y imprimimos el tamaño del recurso. ContentLength
				System.out.println("Tamaño del recurso : "+conexion.getContentLength()+" bytes");
				
				/* 8. Guardamos el stream a un fichero con el nombre del recurso
			    en caso de que el código sea correcto.*/
				if(conexion.getResponseCode() == 200){
					String nomFichero = nombreRecurso(resource);//Daremos al fichero el nombre del recurso de la url
					File fichero = new File(nomFichero);
				
					int bin = -1;
					byte[] buf = new byte[2048];
					InputStream input = conexion.getInputStream();
					OutputStream output = new FileOutputStream(fichero);
				
					while((bin = input.read(buf)) != -1){
						output.write(buf, 0, bin);
					}
					if(fichero.exists() && fichero.length() > 0){ //Si el fichero se crea y se escribe en él
						System.out.println("Fichero '"+nomFichero+"' creado con exito");
					}else{
						System.out.println("Error eal escribir el fichero...");
					}
					
					input.close();
					output.close();
				}else{
					// 9. Damos un error en caso de que el código sea incorrecto (BufferedInputStream -> FileOutputStream)
					System.out.println("ERROR : "+conexion.getResponseCode()+" "+conexion.getResponseMessage());//Error código y mensaje
				}
			}else{ //tam != 0 o el recurso no es una imagen
				System.out.println("Recurso no encontrado");
			}	
		}
		
	}
	
	public static String nombreRecurso(String res){
		String recurso = "";
		StringTokenizer st = new StringTokenizer(res,"/");//Troceamos el string de la url
		while(st.hasMoreElements()){
			recurso = st.nextToken();//Se guardará el último token que es el nombre del recurso
		}
		st = new StringTokenizer(recurso,".");//Separamos el nombre del recurso y su extensión
		String fichero = st.nextToken();//Nos quedamos con el primer token desechamos el resto
		fichero = fichero.concat(".dat");//Añadimos la extensión de nuestro fichero binario
		return fichero;
	}

}
