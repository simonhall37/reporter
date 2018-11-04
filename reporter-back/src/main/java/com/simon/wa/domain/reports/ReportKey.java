package com.simon.wa.domain.reports;

public class ReportKey implements Comparable<ReportKey> {

	private Object[] values;
	
	public ReportKey() {
		
	}
	
	public ReportKey(Object[] values) {
		this.values = values;
	}
	
	@Override
	public int compareTo(ReportKey arg0) {
		int index =0;
		for (Object val : this.values) {
			int current = 0;
			if (val instanceof String) {
				try{
					current = ((String)val).compareTo((String) arg0.findByIndex(index));
					if (current!=0) return current;
					else index++;
				} catch (IndexOutOfBoundsException e) {
					System.out.println("Index Out Of Bounds! " + index);
				} catch (ClassCastException e) {
					System.out.println("Class Cast Exception! " + arg0.findByIndex(index) + " " + e.getMessage());
				}
			}
			else if (val instanceof Integer) {
				try{
					current = ((Integer)val).compareTo((Integer) arg0.findByIndex(index));
					if (current!=0) return current;
					else index++;
				} catch (IndexOutOfBoundsException e) {
					System.out.println("Index Out Of Bounds! " + index);
				} catch (ClassCastException e) {
					System.out.println("Class Cast Exception! " + arg0.findByIndex(index) + " " + e.getMessage());
				}
			}
			else if (val instanceof Double) {
				try{
					current = ((Double)val).compareTo((Double) arg0.findByIndex(index));
					if (current!=0) return current;
					else index++;
				} catch (IndexOutOfBoundsException e) {
					System.out.println("Index Out Of Bounds! " + index);
				} catch (ClassCastException e) {
					System.out.println("Class Cast Exception! " + arg0.findByIndex(index) + " " + e.getMessage());
				}
			}
		}
		return 0;
	}
	
	public void addValue(int i, Object outputValue) {
		this.values[i] = outputValue;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Object o : this.values) {
			sb.append(o + ",");
		}
		return sb.toString().substring(0,Math.max(0,sb.length()-1));
	}
	
	public Object findByIndex(int index) throws IndexOutOfBoundsException {
		return this.values[index];
	}

	public Object[] getValues() {
		return values;
	}

	public void setValues(Object[] values) {
		this.values = values;
	}

	


	
}
