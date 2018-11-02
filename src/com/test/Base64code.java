package com.test;

public class Base64code {
	
	private static final byte[] encodingTable = {
            (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E',
            (byte) 'F', (byte) 'G', (byte) 'H', (byte) 'I', (byte) 'J',
            (byte) 'K', (byte) 'L', (byte) 'M', (byte) 'N', (byte) 'O',
            (byte) 'P', (byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T',
            (byte) 'U', (byte) 'V', (byte) 'W', (byte) 'X', (byte) 'Y',
            (byte) 'Z', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd',
            (byte) 'e', (byte) 'f', (byte) 'g', (byte) 'h', (byte) 'i',
            (byte) 'j', (byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n',
            (byte) 'o', (byte) 'p', (byte) 'q', (byte) 'r', (byte) 's',
            (byte) 't', (byte) 'u', (byte) 'v', (byte) 'w', (byte) 'x',
            (byte) 'y', (byte) 'z', (byte) '0', (byte) '1', (byte) '2',
            (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7',
            (byte) '8', (byte) '9', (byte) '+', (byte) '/'
        };
	
    private static final byte[] decodingTable;
    static {
        decodingTable = new byte[128];
        for (int i = 0; i < 128; i++) {
            decodingTable[i] = (byte) -1;
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            decodingTable[i] = (byte) (i - 'A');
        }
        for (int i = 'a'; i <= 'z'; i++) {
            decodingTable[i] = (byte) (i - 'a' + 26);
        }
        for (int i = '0'; i <= '9'; i++) {
            decodingTable[i] = (byte) (i - '0' + 52);
        }
        decodingTable['+'] = 62;
        decodingTable['/'] = 63;
    }
    
    //解码
    public static int[] decode(byte[] data) {
        int[] in;
        int b1;
        int b2;
        int b3;
        int b4;
        data = discardNonBase64Bytes(data);
        if (data[data.length - 2] == '=') {
            in = new int[(((data.length / 4) - 1) * 3) + 1];
        } else if (data[data.length - 1] == '=') {
            in = new int[(((data.length / 4) - 1) * 3) + 2];
        } else {
            in = new int[((data.length / 4) * 3)];
        }
        for (int i = 0, j = 0; i < (data.length - 4); i += 4, j += 3) {
            b1 = decodingTable[data[i]];
            b2 = decodingTable[data[i + 1]];
            b3 = decodingTable[data[i + 2]];
            b4 = decodingTable[data[i + 3]];
            System.out.println(b1+b2+b3+b4);
            in[j] = ((b1 << 2) | (b2 >> 4)) & 0x000000ff;
            in[j + 1] =((b2 << 4) | (b3 >> 2)) & 0x000000ff;
            in[j + 2] =((b3 << 6) | b4) & 0x000000ff;
        }
        if (data[data.length - 2] == '=') {
            b1 = decodingTable[data[data.length - 4]];
            b2 = decodingTable[data[data.length - 3]];
            in[in.length - 1] = ((b1 << 2) | (b2 >> 4)) & 0x000000ff;
        } else if (data[data.length - 1] == '=') {
            b1 = decodingTable[data[data.length - 4]];
            b2 = decodingTable[data[data.length - 3]];
            b3 = decodingTable[data[data.length - 2]];
            in[in.length - 2] = ((b1 << 2) | (b2 >> 4)) & 0x000000ff;
            in[in.length - 1] = ((b2 << 4) | (b3 >> 2)) & 0x000000ff;
        } else {
            b1 = decodingTable[data[data.length - 4]];
            b2 = decodingTable[data[data.length - 3]];
            b3 = decodingTable[data[data.length - 2]];
            b4 = decodingTable[data[data.length - 1]];
            in[in.length - 3] = ((b1 << 2) | (b2 >> 4)) & 0x000000ff;
            in[in.length - 2] = ((b2 << 4) | (b3 >> 2)) & 0x000000ff;
            in[in.length - 1] = ((b3 << 6) | b4) & 0x000000ff;
        }
        return in;
    }
    public static int[] decode(String data) {
        int[] in;
        int b1;
        int b2;
        int b3;
        int b4;
        data = discardNonBase64Chars(data);
        if (data.charAt(data.length() - 2) == '=') {
            in = new int[(((data.length() / 4) - 1) * 3) + 1];
        } else if (data.charAt(data.length() - 1) == '=') {
            in = new int[(((data.length() / 4) - 1) * 3) + 2];
        } else {
            in = new int[((data.length() / 4) * 3)];
        }
        for (int i = 0, j = 0; i < (data.length() - 4); i += 4, j += 3) {
            b1 = decodingTable[data.charAt(i)];
            b2 = decodingTable[data.charAt(i + 1)];
            b3 = decodingTable[data.charAt(i + 2)];
            b4 = decodingTable[data.charAt(i + 3)];
            in[j] = ((b1 << 2) | (b2 >> 4)) & 0x000000ff;
            in[j + 1] =((b2 << 4) | (b3 >> 2)) & 0x000000ff;         
            in[j + 2] = ((b3 << 6) | b4) & 0x000000ff;
           System.out.print(in[j]+" ");System.out.print(in[j+1]+" ");System.out.println(in[j+2]);
        }
        if (data.charAt(data.length() - 2) == '=') {
            b1 = decodingTable[data.charAt(data.length() - 4)];
            b2 = decodingTable[data.charAt(data.length() - 3)];
            in[in.length - 1] = ((b1 << 2) | (b2 >> 4)) & 0x000000ff;
            System.out.println(in[in.length-1]);
        } else if (data.charAt(data.length() - 1) == '=') {
            b1 = decodingTable[data.charAt(data.length() - 4)];
            b2 = decodingTable[data.charAt(data.length() - 3)];
            b3 = decodingTable[data.charAt(data.length() - 2)];
            in[in.length - 2] = ((b1 << 2) | (b2 >> 4)) & 0x000000ff;
            in[in.length - 1] = ((b2 << 4) | (b3 >> 2)) & 0x000000ff;
        } else {
            b1 = decodingTable[data.charAt(data.length() - 4)];
            b2 = decodingTable[data.charAt(data.length() - 3)];
            b3 = decodingTable[data.charAt(data.length() - 2)];
            b4 = decodingTable[data.charAt(data.length() - 1)];
            in[in.length - 3] = ((b1 << 2) | (b2 >> 4)) & 0x000000ff;
            in[in.length - 2] = ((b2 << 4) | (b3 >> 2)) & 0x000000ff;
            in[in.length - 1] = ((b3 << 6) | b4) & 0x000000ff;
        }
        return in;
    }
    //检查是否有非Base64字符
    private static byte[] discardNonBase64Bytes(byte[] data) {
        byte[] temp = new byte[data.length];
        int bytesCopied = 0;
        for (int i = 0; i < data.length; i++) {
            if (isValidBase64Byte(data[i])) {
                temp[bytesCopied++] = data[i];
            }
        }
        byte[] newData = new byte[bytesCopied];
        System.arraycopy(temp, 0, newData, 0, bytesCopied);
        return newData;
    }
    private static String discardNonBase64Chars(String data) {
        StringBuffer sb = new StringBuffer();
        int length = data.length();
        for (int i = 0; i < length; i++) {
            if (isValidBase64Byte((byte) (data.charAt(i)))) {
                sb.append(data.charAt(i));
            }
        }
        return sb.toString();
    }
    
    private static boolean isValidBase64Byte(byte b) {
        if (b == '=') {
            return true;
        } else if ((b < 0) || (b >= 128)) {
            return false;
        } else if (decodingTable[b] == -1) {
            return false;
        }
        return true;
    }
	
    public static void main(String[] args) throws Exception{ 
			
		String str = "EjSrzQEOISv1/BERAACj/w==";
		
		int[] data = Base64code.decode(str);
		int[] num = new int[data.length/2];
		for(int i=0,j=0; i< data.length;i+=2,j++) {
			num[j] = (data[i] << 8) | data[i+1];
			System.out.println(Integer.toHexString(data[i])+Integer.toHexString(data[i+1])+":"+num[j]);
		}
		
		//12 34 ab cd 01 0e 21 2b f5 fc 11 11 00 00 a3 ff  EjSrzQEOISv1/BERAACj/w==
		//00 00 a3 7f 6a ff 01 3c 00 07 bc 4a 00 01 00 14  AACjf2r/ATwAB7xKAAEAFA==
		//11 12 3c 6f 02 45 8c 2e ERI8bwJFjC4=
		//a3 a3 a3 a3  o6Ojow==
		//70 7f 80 cH+A
		//gIGCg4SFhoeIiYqLjI2Ojw==
		
    }
}

