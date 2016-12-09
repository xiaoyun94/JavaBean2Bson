# JavaBean2Bson
This tool can transform a java bean to bson document, which is used in MongoDB. </br>
It will help programmer of site background. </br>
Due to the api supports of BSON, it can transform into JSON as well. 

## Requirement
Please do not use old version for BSON dependence, new Java drive can be compatiable to old Mongo version.</br>
BSON dependence(I provide 3.4 version in my package)

## Usage
you can download the binary jar at:</br>
https://github.com/xiaoyun94/JavaBean2Bson/blob/master/bean2bson.jar</br>
This jar may be not updated as the code updates. please attention to the date of release.

## Example

```
import java.util.Date;

import cn.bigforce.bean2bson.Bean2Bson;

public class Test {
	Date date = new Date();
	public static void main(String[] args) {
		System.out.println(Bean2Bson.analysisInstance(new Test()));
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
}
```

