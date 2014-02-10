package api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Observable;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.text.format.Time;
import android.util.Log;

public class API extends Observable implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7109339009638097277L;
	/**
	 * 
	 */
	private String bitaddress, lastError, hash_str;
	private static final String site = "http://wafflepool.com/tmp_api?address=";
	private Time lastChecked;
	/**
	 * @return the bitaddress
	 */
	public String getBitaddress() {
		return bitaddress;
	}

	/**
	 * @param bitaddress the bitaddress to set
	 */
	public void setBitaddress(String bitaddress) {
		this.bitaddress = bitaddress;
	}

	/**
	 * @return the lastError
	 */
	public String getLastError() {
		return lastError;
	}

	/**
	 * @return the hash_str
	 */
	public String getHash_str() {
		return hash_str;
	}

	/**
	 * @return the lastChecked
	 */
	public Time getLastChecked() {
		return lastChecked;
	}
	
	public String getLastCheckedString() {
		if (lastChecked != null) {
			return lastChecked.format("%H:%M:%S");
		} else {
			return "Not checked";
		}
	}

	/**
	 * @return the paid
	 */
	public float getPaid() {
		return paid;
	}

	/**
	 * @return the confirmed
	 */
	public float getConfirmed() {
		return confirmed;
	}

	/**
	 * @return the unconverted
	 */
	public float getUnconverted() {
		return unconverted;
	}

	/**
	 * @return the hashrate
	 */
	public int getHashrate() {
		return hashrate;
	}

	private float paid, confirmed, unconverted;
	private int hashrate;
	
	public API(String _addr) {
		bitaddress = _addr;
		lastChecked = new Time();
	}
	
	public Boolean update() {
		DefaultHttpClient client = new DefaultHttpClient();
		if (bitaddress.length() < 1) return false;
		String response = "";
		HttpGet httpGet = new HttpGet(site + bitaddress);
		try {
			HttpResponse execute = client.execute(httpGet);
			InputStream content = execute.getEntity().getContent();

			BufferedReader buffer = new BufferedReader(
					new InputStreamReader(content));
			String s = "";
			while ((s = buffer.readLine()) != null) {
				response += s;
			}

		} catch (Exception e) {
			Log.e("API", "Error!!! " + e.getLocalizedMessage());
			e.printStackTrace();
			lastError = e.getLocalizedMessage();
			return false;
		}
		JSONObject js = null;
		try {
			js = new JSONObject(response);
			lastError = js.getString("error");
			this.setChanged();
			hashrate = Integer.parseInt(js.getString("hash_rate"));
			hash_str = js.getString("hash_rate_str");
			js = js.getJSONObject("balances");
			paid = Float.parseFloat(js.getString("sent"));
			confirmed = Float.parseFloat(js.getString("confirmed"));
			unconverted = Float.parseFloat(js.getString("unconverted"));
		} catch (Exception e) {
		}
		finally {
			this.notifyObservers();
		}
		lastChecked.setToNow();
		return true;
	}

}
