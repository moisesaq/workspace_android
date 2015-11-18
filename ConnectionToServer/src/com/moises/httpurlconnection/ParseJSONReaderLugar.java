package com.moises.httpurlconnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.util.JsonReader;

public class ParseJSONReaderLugar {
	
	InputStream is;
	public ParseJSONReaderLugar(InputStream is){
		this.is = is;
	}
	
	public List<Lugar> readJsonStream() throws IOException{
		JsonReader reader = new JsonReader(new InputStreamReader(is));
		try{
			//Leer la lista de array (json)
			return leerArrayLugares(reader);
		}finally{
			reader.close();
		}
	}

	private List leerArrayLugares(JsonReader reader) throws IOException {
		ArrayList lista_lugares = new ArrayList();
		reader.beginArray();
		while(reader.hasNext()){
			lista_lugares.add(leerLugar(reader));
		}
		reader.endArray();
		return lista_lugares;
	}

	private Object leerLugar(JsonReader reader) throws IOException{
		String idlugar = null;
		String nombre_lugar = null;
		String direccion = null;
		String descripcion = null;
		
		reader.beginObject();
		while(reader.hasNext()){
			//Lectura a cada atributo dentro de objeto en array json. 
			//NOTA: No lo hice con switch porque recien desde java 1.7 se puedo comparar String
			String name = reader.nextName();
			
			if(name.equals("idlugar")){
				idlugar = reader.nextString();
			}else if(name.equals("nombre_lugar")){
				nombre_lugar = reader.nextString();
			}else if(name.equals("direccion")){
				direccion = reader.nextString();
			}else if(name.equals("descripcion")){
				descripcion = reader.nextString();
			}else{
				reader.skipValue();
			}
		}
		reader.endObject();
		return new Lugar(idlugar, nombre_lugar, direccion, descripcion);
	}
}
