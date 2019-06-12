package com.theorydance.myone.common.utils;

import java.util.HashMap;
import java.util.Map;

public class AqiUtils {

	public static final double[] AQI 	= new double[]{0, 50,100,150,200, 300, 400, 500}; 
	public static final double[] SO2_24 = new double[]{0, 50,150,475,800,1600,2100,2620}; 
	public static final double[] SO2_1	= new double[]{0,150,500,650,800,1600,2100,2620}; 
	public static final double[] NO2_24 = new double[]{0, 40, 80,180,280, 565, 750, 940}; 
	public static final double[] NO2_1	= new double[]{0,100,200,700,1200,2340,3090,3840}; 
	public static final double[] PM10 	= new double[]{0, 50,150,250,350, 420, 500, 600}; 
	public static final double[] CO_24 	= new double[]{0,  2,  4, 14, 24,  36,  48,  60}; 
	public static final double[] CO_1 	= new double[]{0,  5, 10, 35, 60,  90, 120, 150}; 
	public static final double[] O3_1 	= new double[]{0,160,200,300,400,800,1000,1200}; 
	public static final double[] O3_8 	= new double[]{0,100,160,215,265,800,1000,1200}; 
	public static final double[] PM25 	= new double[]{0, 35, 75,115,150,250, 350, 500}; 
	
	/**
	 * 通过分因子获取分aqi
	 * @param factor
	 * @return
	 */
	public static int getIAQIbyFactor(String factor,Double val){
		double value = 0;
		if(val != null){
			value = val;
		}
		double IH = 0;
		double IL = 0;
		double BH = 0;
		double BL = 0;
		switch (factor.toLowerCase()) {
		case "so2_1":
		{
			int x = calcStart(SO2_1,value);
			if(x==0){
				return 0;
			}
			IH = AQI[x];
			IL = AQI[x-1];
			BH = SO2_1[x];
			BL = SO2_1[x-1];
			break;
		}
		case "so2_24":
		{
			int x = calcStart(SO2_24,value);
			if(x==0){
				return 0;
			}
			IH = AQI[x];
			IL = AQI[x-1];
			BH = SO2_24[x];
			BL = SO2_24[x-1];
			break;
		}
		case "no2_24":
		{
			int x = calcStart(NO2_24,value);
			if(x==0){
				return 0;
			}
			IH = AQI[x];
			IL = AQI[x-1];
			BH = NO2_24[x];
			BL = NO2_24[x-1];
			break;
		}
		case "no2_1":
		{
			int x = calcStart(NO2_1,value);
			if(x==0){
				return 0;
			}
			IH = AQI[x];
			IL = AQI[x-1];
			BH = NO2_1[x];
			BL = NO2_1[x-1];
			break;
		}
		case "o3_1":
		{
			int x = calcStart(O3_1,value);
			if(x==0){
				return 0;
			}
			IH = AQI[x];
			IL = AQI[x-1];
			BH = O3_1[x];
			BL = O3_1[x-1];
			break;
		}
		case "o3_8":
		{
			int x = calcStart(O3_8,value);
			if(x==0){
				return 0;
			}
			IH = AQI[x];
			IL = AQI[x-1];
			BH = O3_8[x];
			BL = O3_8[x-1];
			break;
		}
		case "pm10":
		{
			int x = calcStart(PM10,value);
			if(x==0){
				return 0;
			}
			IH = AQI[x];
			IL = AQI[x-1];
			BH = PM10[x];
			BL = PM10[x-1];
			break;
		}
		case "pm25":
		{
			int x = calcStart(PM25,value);
			if(x==0){
				return 0;
			}
			IH = AQI[x];
			IL = AQI[x-1];
			BH = PM25[x];
			BL = PM25[x-1];
			break;
		}
		case "co_1":
		{
			int x = calcStart(CO_1,value);
			if(x==0){
				return 0;
			}
			IH = AQI[x];
			IL = AQI[x-1];
			BH = CO_1[x];
			BL = CO_1[x-1];
			break;
		}
		case "co_24":
		{
			int x = calcStart(CO_24,value);
			if(x==0){
				return 0;
			}
			IH = AQI[x];
			IL = AQI[x-1];
			BH = CO_24[x];
			BL = CO_24[x-1];
			break;
		}

		//yeafel修改，按照国际标准，此处要加上default,即使default里面什么都不存在。
			default:
		}
		if(IH>0){
			return (int)(Math.ceil((IH-IL)*(value-BL)/(BH-BL) + IL));
		}
		return 0;
	}
	
	// 计算value在数组中应该处于的位置下标
	private static int calcStart(double[] array,Double val){
		double value = 0;
		if(val != null){
			value = val;
		}
		int position = -1;
		int num = array.length;
		double err = 0.001;
		for (int i = 0; i < num; i++) {
			double temp = array[i];
			if(value+err <= temp || value-err<=temp){
				position = i;
				break;
			}
		}
		if(position<0){
			position = num - 1;
		}
		return position;
	}
	
	/**
	 * 通过6个因子获取总AQI
	 * @return
	 */
	public static Map<String, Object> getAQIby6Factor(String type,Double pm10D,Double pm25D,Double so2D,Double no2D,Double coD,Double o3D,Double o3_8D){
		int nnn = 0;
		double pm10 = 0;
		double pm25 = 0;
		double so2 = 0;
		double no2 = 0;
		double co = 0;
		double o3 = 0;
		double o3_8 = 0;
		if(pm10D!=null){
			pm10 = pm10D;
			nnn ++;
		}
		if(pm25D!=null){
			pm25 = pm25D;
			nnn ++;
		}
		if(so2D!=null){
			so2 = so2D;
			nnn ++;
		}
		if(no2D!=null){
			no2 = no2D;
			nnn ++;
		}
		if(coD!=null){
			co = coD;
			nnn ++;
		}
		if(o3D!=null){
			o3 = o3D;
			nnn ++;
		}
		if(o3_8D!=null&&o3_8D>1){
			o3_8 = o3_8D;
			nnn ++;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		int iPM10 = 0;
		int iPM25 = 0;
		int iSO2 = 0;
		int iNO2 = 0;
		int iCO = 0;
		int iO3 = 0;
		int iO3_8 = 0;
		String primary = "PM10";
		int aqi = iPM10;
		
		if(type.equals("hour")){
			iPM10 = getIAQIbyFactor("pm10",pm10);
			iPM25 = getIAQIbyFactor("pm25",pm25);
			iSO2 = getIAQIbyFactor("so2_1",so2);
			iNO2 = getIAQIbyFactor("no2_1",no2);
			iCO = getIAQIbyFactor("co_1",co);
		    iO3 = getIAQIbyFactor("o3_1",o3);
		    if(aqi<iO3){
				aqi = iO3;
				primary = "O3";
			}else if(aqi == iO3){
				primary += ",O3";
			}
		}else if(type.equals("day")){
			iPM10 = getIAQIbyFactor("pm10",pm10);
			iPM25 = getIAQIbyFactor("pm25",pm25);
			iSO2 = getIAQIbyFactor("so2_24",so2);
			iNO2 = getIAQIbyFactor("no2_24",no2);
			iCO = getIAQIbyFactor("co_24",co);
			if(o3_8>0 && o3_8<800){
				iO3_8 = getIAQIbyFactor("o3_8",o3_8);
				if(aqi<iO3_8){
					aqi = iO3_8;
					primary = "O3_8h";
				}else if(aqi == iO3_8){
					primary += ",O3_8h";
				}
			}else{
				iO3 = getIAQIbyFactor("o3_1",o3);
			    if(aqi<iO3){
					aqi = iO3;
					primary = "O3";
				}else if(aqi == iO3){
					primary += ",O3";
				}
			}
		}
		
		if(aqi<iPM25){
			aqi = iPM25;
			primary = "PM2.5";
		}else if(aqi == iPM25){
			primary += ",PM2.5";
		}
		
		if(aqi<iPM10){
			aqi = iPM10;
			primary = "PM10";
		}else if(aqi == iPM10){
			primary += ",PM10";
		}
		
		if(aqi<iSO2){
			aqi = iSO2;
			primary = "SO2";
		}else if(aqi == iSO2){
			primary += ",SO2";
		}
		
		if(aqi<iNO2){
			aqi = iNO2;
			primary = "NO2";
		}else if(aqi == iNO2){
			primary += ",NO2";
		}
		
		if(aqi<iCO){
			aqi = iCO;
			primary = "CO";
		}else if(aqi == iCO){
			primary += ",CO";
		}
		
		if(aqi<5 || nnn<=2){
			map.put("aqi", 0);
			map.put("primary_pollu", "--");
		}else if(aqi<=50){// aqi 为忧时无首要污染物
			map.put("aqi", aqi);
			map.put("primary_pollu", "--");
			return map;
		}else{
			map.put("aqi", aqi>500?500:aqi);
			map.put("primary_pollu", primary);
		}
		return map;
	}
	
	/**
	 * 通过aqi判定空气质量等级
	 * @param aqi 传递AQI数值，如果为0或空，返回离线（0）
	 * @return
	 * 0(离线或数据不纳入计算),1(优),2(良),...,6(严重)
	 */
	public static int getLevelByAQI(Double aqiD){
		double aqi = 0;
		if(aqiD != null){
			aqi = aqiD;
		}
		if(aqi < 1){
			return 0; // 表示离线
		}else if(aqi <= AQI[1]){ // 优 绿色 一级
			return 1;
		}else if(aqi <= AQI[2]){ // 良 黄色 二级
			return 2;
		}else if(aqi <= AQI[3]){ // 轻度污染 橙色 三级
			return 3;
		}else if(aqi <= AQI[4]){ // 中度污染 红色 四级
			return 4;
		}else if(aqi <= AQI[5]){ // 重度污染 紫色 五级
			return 5;
		}else{ // 严重污染 褐红色 六级
			return 6;
		}
	}
	
	
	/**
	 * 通过aqi判定空气质量等级
	 * @param aqi 传递AQI数值，如果为0或空，返回离线（0）
	 * @return
	 * 0(离线或数据不纳入计算),1(优),2(良),...,6(严重)
	 */
	public static String getQualityByAQI(int aqiD){
		int aqi = 0;
		if(aqiD != 0){
			aqi = aqiD;
		}
		if(aqi < 1){
			return "--"; // 表示离线
		}else if(aqi <= AQI[1]){ // 优 绿色 一级
			return "优";
		}else if(aqi <= AQI[2]){ // 良 黄色 二级
			return "良";
		}else if(aqi <= AQI[3]){ // 轻度污染 橙色 三级
			return "轻度污染";
		}else if(aqi <= AQI[4]){ // 中度污染 红色 四级
			return "中度污染";
		}else if(aqi <= AQI[5]){ // 重度污染 紫色 五级
			return "重度污染";
		}else{ // 严重污染 褐红色 六级
			return "严重污染";
		}
	}
	
	/**
	 * 
	* @Title: getQuality
	* @Description: TODO(获取优良等级)
	* @param @param quality
	* @param @return    设定文件
	* @return String    返回类型
	* @throws
	 */
	public static String getQuality(int quality){
		String res =null; 
		switch (quality) {
		case 1:
			res ="优";
			break;
		case 2:
			res = "良";
			break;
		case 3:
			res = "轻度污染";
			break;
		case 4:
			res = "中度污染";
			break;
		case 5:
			res = "重度污染";
			break;
		case 6:
			res ="严重污染";
			break;

		//yeafel修改，按照国际标准，此处要加上default,即使default里面什么都不存在。
		default:
		}
		return res ;
	}
	
	public static Map<String, Object> getAQIby6FactorArm(String type,Double pm10D,Double pm25D,Double so2D,Double no2D,Double coD,Double o3D,Double o3_8D){
		int nnn = 0;
		double pm10 = 0;
		double pm25 = 0;
		double so2 = 0;
		double no2 = 0;
		double co = 0;
		double o3 = 0;
		double o3_8 = 0;
		if(pm10D!=null){
			pm10 = pm10D;
			nnn ++;
		}
		if(pm25D!=null){
			pm25 = pm25D;
			nnn ++;
		}
		if(so2D!=null){
			so2 = so2D;
			nnn ++;
		}
		if(no2D!=null){
			no2 = no2D;
			nnn ++;
		}
		if(coD!=null){
			co = coD;
			nnn ++;
		}
		if(o3D!=null){
			o3 = o3D;
			nnn ++;
		}
		if(o3_8D!=null&&o3_8D>1){
			o3_8 = o3_8D;
			nnn ++;
			if(o3_8<=800){
				o3 = 0;
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		int iPM10 = 0;
		int iPM25 = 0;
		int iSO2 = 0;
		int iNO2 = 0;
		int iCO = 0;
		int iO3 = 0;
		int iO3_8 = 0;
		
		if(type.equals("hour")){
			iPM10 = getIAQIbyFactor("pm10",pm10);
			iPM25 = getIAQIbyFactor("pm25",pm25);
			iSO2 = getIAQIbyFactor("so2_1",so2);
			iNO2 = getIAQIbyFactor("no2_1",no2);
			iCO = getIAQIbyFactor("co_1",co);
			if(o3_8>0 && o3_8<800){
				iO3_8 = getIAQIbyFactor("o3_8",o3_8);
			}else{
				iO3 = getIAQIbyFactor("o3_1",o3);
			}
		}else if(type.equals("day")){
			iPM10 = getIAQIbyFactor("pm10",pm10);
			iPM25 = getIAQIbyFactor("pm25",pm25);
			iSO2 = getIAQIbyFactor("so2_24",so2);
			iNO2 = getIAQIbyFactor("no2_24",no2);
			iCO = getIAQIbyFactor("co_24",co);
			if(o3_8>0 && o3_8<800){
				iO3_8 = getIAQIbyFactor("o3_8",o3_8);
			}else{
				iO3 = getIAQIbyFactor("o3_1",o3);
			}
		}
		
		String primary = "PM10";
		int aqi = iPM10;
		
		if(aqi<iPM25){
			aqi = iPM25;
			primary = "PM2.5";
		}else if(aqi == iPM25){
			primary += ",PM2.5";
		}
		
		if(aqi<iSO2){
			aqi = iSO2;
			primary = "SO2";
		}else if(aqi == iSO2){
			primary += ",SO2";
		}
		
		if(aqi<iNO2){
			aqi = iNO2;
			primary = "NO2";
		}else if(aqi == iNO2){
			primary += ",NO2";
		}
		
		if(aqi<iCO){
			aqi = iCO;
			primary = "CO";
		}else if(aqi == iCO){
			primary += ",CO";
		}
		
		if(aqi<iO3){
			aqi = iO3;
			primary = "O3";
		}else if(aqi == iO3){
			primary += ",O3";
		}
		
		if(aqi<iO3_8){
			aqi = iO3_8;
			primary = "O3_8h";
		}else if(aqi == iO3_8){
			primary += ",O3_8h";
		}
//		if(aqi<5 || nnn<=2){
//			map.put("aqi", 0);
//			map.put("primary_pollu", "--");
//		}else 
		if(aqi<=50){// aqi 为忧时无首要污染物
			map.put("aqi", aqi);
			map.put("primary_pollu", "--");
			return map;
		}else{
			map.put("aqi", aqi>500?500:aqi);
			map.put("primary_pollu", primary);
		}
		return map;
	}
}
