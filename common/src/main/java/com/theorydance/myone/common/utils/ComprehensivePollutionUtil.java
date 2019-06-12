package com.theorydance.myone.common.utils;

import java.math.BigDecimal;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/** 
 * @Description: (综合污染指数)
 * @Author gaochao
 * @date   2018-4-11 下午5:14:22
 *	@version V1.0 
 */
@Slf4j
public class ComprehensivePollutionUtil {
	
	public static final double[] SO2_y	= new double[]{60}; 
	public static final double[] SO2_24 = new double[]{150}; 
	
	public static final double[] NO2_y = new double[]{40}; 
	public static final double[] NO2_24 = new double[]{80}; 
	
	
	public static final double[] CO_24 	= new double[]{4}; 
	
	public static final double[] O3_8 	= new double[]{160}; 
	
	public static final double[] PM25_y 	= new double[]{35}; 
	public static final double[] PM25_24 	= new double[]{75}; 
	
	public static final double[] PM10_y 	= new double[]{70}; 
	public static final double[] PM10_24 	= new double[]{150}; 
	
	//污染浓度分指数计算
	public static double getComprehenPIndex(String factor,Double val){
		double par1=0;
		double par2=0;
		if(val==null){
			return 0;
		}
		try {
			switch (factor) {
			case "pm10":
				par1=val/PM10_y[0];
				par2=(val-PM10_24[0])/PM10_24[0];
				break;
			case "pm25":
				par1=val/PM25_y[0];
				par2=(val-PM25_24[0])/PM25_24[0];
				break;	
			case "no2":
				par1=val/NO2_y[0];
				par2=(val-NO2_24[0])/NO2_24[0];
				break;	
			case "so2":
				par1=val/SO2_y[0];
				par2=(val-SO2_24[0])/SO2_24[0];
				break;	
			case "co":
				par1=val/CO_24[0];
				par2=(val-CO_24[0])/CO_24[0];
				break;	
			case "o3_8h":
				par1=val/O3_8[0];
				par2=(val-O3_8[0])/O3_8[0];
				break;	
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}	
		double re = par1>par2?par1:par2;
		BigDecimal bg = new BigDecimal(re);
		return  bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	//污染浓度指数计算
	public static void getComprehenP(Map<String, Object> lmap, Double pm10,Double pm25,Double no2,Double so2, Double co,Double o3_8h ){
		double pm10I = getComprehenPIndex("pm10", pm10);
		double pm25I = getComprehenPIndex("pm25", pm25);
		double no2I = getComprehenPIndex("no2", no2);
		double so2I = getComprehenPIndex("so2", so2);
		double coI = getComprehenPIndex("co", co);
		double o3_8hI = getComprehenPIndex("o3_8h", o3_8h);
		lmap.put("pm10I", pm10I);
		lmap.put("pm25I", pm25I);
		lmap.put("no2I", no2I);
		lmap.put("so2I", so2I);
		lmap.put("coI", coI);
		lmap.put("o3_8hI",o3_8hI);
		BigDecimal b = new BigDecimal(pm10I+pm25I+no2I+so2I+coI+o3_8hI);
		lmap.put("c_pollution", b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
	
	//污染浓度指数计算


	public static void getComprehenP1(Map<String, Object> lmap, Double pm10,Double pm25,Double no2,Double so2, Double co,Double o3_8h ){
		double pm10I = getComprehenPIndex("pm10", pm10);
		double pm25I = getComprehenPIndex("pm25", pm25);
		double no2I = getComprehenPIndex("no2", no2);
		double so2I = getComprehenPIndex("so2", so2);
		double coI = getComprehenPIndex("co", co);
		double o3_8hI = getComprehenPIndex("o3_8h", o3_8h);
		BigDecimal b = new BigDecimal(pm10I+pm25I+no2I+so2I+coI+o3_8hI);
		lmap.put("c_pollution", b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
}
