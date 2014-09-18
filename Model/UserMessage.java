package Model;

import java.io.Serializable;

public class UserMessage implements Serializable {
	String id;
	String ip;
	int code;
	String message;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UserMessage(String id, String ip, int code, String message) {
		this.id = id;
		this.ip = ip;
		this.code = code;
		this.message = message;
	}

	public String toString() {
		return id + ip + code + message;
	}
}
