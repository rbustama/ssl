import java.io.Serializable;


public class Request implements Serializable {
	TypeRequest type;

	public Request(TypeRequest type) {
		this.type = type;
	}
	
}
