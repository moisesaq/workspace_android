package com.moises.httpurlconnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;


public class GsonParseLugar {

	InputStream istream;
	
	public GsonParseLugar(InputStream istream){
		this.istream = istream;
	}
	
	public List<Lugar> getLugares() throws IOException{
		List<Lugar> lista = new ArrayList<Lugar>();
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new InputStreamReader(istream, "UTF-8"));
		
		reader.beginArray();
		while(reader.hasNext()){
			Lugar lugar = gson.fromJson(reader, Lugar.class);
			lista.add(lugar);
		}
		reader.endArray();
		reader.close();
		return lista;
	}
}
