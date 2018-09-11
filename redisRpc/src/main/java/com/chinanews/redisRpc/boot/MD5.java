package com.chinanews.redisRpc.boot;

/**
 * md5
 * @Description:TODO
 * @author:yy
 * @time:2018年9月4日 上午9:15:26
 */
public class MD5
{
    class MD5_CTX
    {

        long state[];
        long count[];
        byte buffer[];
        
        MD5_CTX()
        {
            state = new long[4];
            count = new long[2];
            buffer = new byte[64];
            
        }
    }


    static final char HEX[] = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
        'a', 'b', 'c', 'd', 'e', 'f'
    };
    static final int S11 = 7;
    static final int S12 = 12;
    static final int S13 = 17;
    static final int S14 = 22;
    static final int S21 = 5;
    static final int S22 = 9;
    static final int S23 = 14;
    static final int S24 = 20;
    static final int S31 = 4;
    static final int S32 = 11;
    static final int S33 = 16;
    static final int S34 = 23;
    static final int S41 = 6;
    static final int S42 = 10;
    static final int S43 = 15;
    static final int S44 = 21;
    static final byte PADDING[];

    public MD5()
    {
    }

    public String doDigest(String s)
    {
        byte abyte0[] = doDigest(s.getBytes());
        StringBuffer stringbuffer = new StringBuffer();
        for(int i = 0; i < abyte0.length; i++)
        {
            stringbuffer.append(HEX[byte2int(abyte0[i]) / 16]);
            stringbuffer.append(HEX[byte2int(abyte0[i]) % 16]);
        }

        return stringbuffer.toString();
    }

    public byte[] doDigest(byte abyte0[])
    {
        MD5_CTX md5_ctx = new MD5_CTX();
        byte abyte1[] = new byte[16];
        int i = abyte0.length;
        MD5Init(md5_ctx);
        MD5Update(md5_ctx, abyte0, i);
        MD5Final(abyte1, md5_ctx);
        return abyte1;
    }

    long fix(long l)
    {
        return l & 0xffffffffL;
    }

    long F(long l, long l1, long l2)
    {
        return fix(l & l1 | ~l & l2);
    }

    long G(long l, long l1, long l2)
    {
        return fix(l & l2 | l1 & ~l2);
    }

    long H(long l, long l1, long l2)
    {
        return fix(l ^ l1 ^ l2);
    }

    long I(long l, long l1, long l2)
    {
        return fix(l1 ^ (l | ~l2));
    }

    long ROTATE_LEFT(long l, long l1)
    {
        return fix(l << (int)l1 | l >> (int)(32L - l1));
    }

    void FF(long al[], int i, int j, int k, int l, long l1, 
            long l2, long l3)
    {
        al[i] += F(al[j], al[k], al[l]) + l1 + l3;
        al[i] = ROTATE_LEFT(fix(al[i]), l2);
        al[i] += al[j];
        al[i] = fix(al[i]);
    }

    void GG(long al[], int i, int j, int k, int l, long l1, 
            long l2, long l3)
    {
        al[i] += G(al[j], al[k], al[l]) + l1 + l3;
        al[i] = ROTATE_LEFT(fix(al[i]), l2);
        al[i] += al[j];
        al[i] = fix(al[i]);
    }

    void HH(long al[], int i, int j, int k, int l, long l1, 
            long l2, long l3)
    {
        al[i] += H(al[j], al[k], al[l]) + l1 + l3;
        al[i] = ROTATE_LEFT(fix(al[i]), l2);
        al[i] += al[j];
        al[i] = fix(al[i]);
    }

    void II(long al[], int i, int j, int k, int l, long l1, 
            long l2, long l3)
    {
        al[i] += I(al[j], al[k], al[l]) + l1 + l3;
        al[i] = ROTATE_LEFT(fix(al[i]), l2);
        al[i] += al[j];
        al[i] = fix(al[i]);
    }

    void MD5Init(MD5_CTX md5_ctx)
    {
        md5_ctx.count[0] = md5_ctx.count[1] = 0L;
        md5_ctx.state[0] = 0x67452301L;
        md5_ctx.state[1] = 0xefcdab89L;
        md5_ctx.state[2] = 0x98badcfeL;
        md5_ctx.state[3] = 0x10325476L;
    }

    void MD5Update(MD5_CTX md5_ctx, byte abyte0[], int i)
    {
        int k = (int)(md5_ctx.count[0] >> 3 & 63L);
        if(fix(md5_ctx.count[0] += (long)i << 3) < fix((long)i << 3))
        {
            md5_ctx.count[1]++;
        }
        md5_ctx.count[1] += (long)i >> 29;
        md5_ctx.count[1] = fix(md5_ctx.count[1]);
        int l = 64 - k;
        int j;
        if(i >= l)
        {
            MD5_memcpy(md5_ctx.buffer, k, abyte0, 0, l);
            MD5Transform(md5_ctx.state, md5_ctx.buffer, 0);
            for(j = l; j + 63 < i; j += 64)
            {
                MD5Transform(md5_ctx.state, abyte0, j);
            }

            k = 0;
        } else
        {
            j = 0;
        }
        MD5_memcpy(md5_ctx.buffer, k, abyte0, j, i - j);
    }

    void MD5Final(byte abyte0[], MD5_CTX md5_ctx)
    {
        byte abyte1[] = new byte[8];
        Encode(abyte1, md5_ctx.count, 8);
        int i = (int)(md5_ctx.count[0] >> 3 & 63L);
        int j = i >= 56 ? 120 - i : 56 - i;
        MD5Update(md5_ctx, PADDING, j);
        MD5Update(md5_ctx, abyte1, 8);
        Encode(abyte0, md5_ctx.state, 16);
    }

    void MD5Transform(long al[], byte abyte0[], int i)
    {
        long al1[] = new long[4];
        System.arraycopy(al, 0, al1, 0, 4);
        long al2[] = new long[16];
        Decode(al2, abyte0, i, 64);
        FF(al1, 0, 1, 2, 3, al2[0], 7L, 0xd76aa478L);
        FF(al1, 3, 0, 1, 2, al2[1], 12L, 0xe8c7b756L);
        FF(al1, 2, 3, 0, 1, al2[2], 17L, 0x242070dbL);
        FF(al1, 1, 2, 3, 0, al2[3], 22L, 0xc1bdceeeL);
        FF(al1, 0, 1, 2, 3, al2[4], 7L, 0xf57c0fafL);
        FF(al1, 3, 0, 1, 2, al2[5], 12L, 0x4787c62aL);
        FF(al1, 2, 3, 0, 1, al2[6], 17L, 0xa8304613L);
        FF(al1, 1, 2, 3, 0, al2[7], 22L, 0xfd469501L);
        FF(al1, 0, 1, 2, 3, al2[8], 7L, 0x698098d8L);
        FF(al1, 3, 0, 1, 2, al2[9], 12L, 0x8b44f7afL);
        FF(al1, 2, 3, 0, 1, al2[10], 17L, 0xffff5bb1L);
        FF(al1, 1, 2, 3, 0, al2[11], 22L, 0x895cd7beL);
        FF(al1, 0, 1, 2, 3, al2[12], 7L, 0x6b901122L);
        FF(al1, 3, 0, 1, 2, al2[13], 12L, 0xfd987193L);
        FF(al1, 2, 3, 0, 1, al2[14], 17L, 0xa679438eL);
        FF(al1, 1, 2, 3, 0, al2[15], 22L, 0x49b40821L);
        GG(al1, 0, 1, 2, 3, al2[1], 5L, 0xf61e2562L);
        GG(al1, 3, 0, 1, 2, al2[6], 9L, 0xc040b340L);
        GG(al1, 2, 3, 0, 1, al2[11], 14L, 0x265e5a51L);
        GG(al1, 1, 2, 3, 0, al2[0], 20L, 0xe9b6c7aaL);
        GG(al1, 0, 1, 2, 3, al2[5], 5L, 0xd62f105dL);
        GG(al1, 3, 0, 1, 2, al2[10], 9L, 0x2441453L);
        GG(al1, 2, 3, 0, 1, al2[15], 14L, 0xd8a1e681L);
        GG(al1, 1, 2, 3, 0, al2[4], 20L, 0xe7d3fbc8L);
        GG(al1, 0, 1, 2, 3, al2[9], 5L, 0x21e1cde6L);
        GG(al1, 3, 0, 1, 2, al2[14], 9L, 0xc33707d6L);
        GG(al1, 2, 3, 0, 1, al2[3], 14L, 0xf4d50d87L);
        GG(al1, 1, 2, 3, 0, al2[8], 20L, 0x455a14edL);
        GG(al1, 0, 1, 2, 3, al2[13], 5L, 0xa9e3e905L);
        GG(al1, 3, 0, 1, 2, al2[2], 9L, 0xfcefa3f8L);
        GG(al1, 2, 3, 0, 1, al2[7], 14L, 0x676f02d9L);
        GG(al1, 1, 2, 3, 0, al2[12], 20L, 0x8d2a4c8aL);
        HH(al1, 0, 1, 2, 3, al2[5], 4L, 0xfffa3942L);
        HH(al1, 3, 0, 1, 2, al2[8], 11L, 0x8771f681L);
        HH(al1, 2, 3, 0, 1, al2[11], 16L, 0x6d9d6122L);
        HH(al1, 1, 2, 3, 0, al2[14], 23L, 0xfde5380cL);
        HH(al1, 0, 1, 2, 3, al2[1], 4L, 0xa4beea44L);
        HH(al1, 3, 0, 1, 2, al2[4], 11L, 0x4bdecfa9L);
        HH(al1, 2, 3, 0, 1, al2[7], 16L, 0xf6bb4b60L);
        HH(al1, 1, 2, 3, 0, al2[10], 23L, 0xbebfbc70L);
        HH(al1, 0, 1, 2, 3, al2[13], 4L, 0x289b7ec6L);
        HH(al1, 3, 0, 1, 2, al2[0], 11L, 0xeaa127faL);
        HH(al1, 2, 3, 0, 1, al2[3], 16L, 0xd4ef3085L);
        HH(al1, 1, 2, 3, 0, al2[6], 23L, 0x4881d05L);
        HH(al1, 0, 1, 2, 3, al2[9], 4L, 0xd9d4d039L);
        HH(al1, 3, 0, 1, 2, al2[12], 11L, 0xe6db99e5L);
        HH(al1, 2, 3, 0, 1, al2[15], 16L, 0x1fa27cf8L);
        HH(al1, 1, 2, 3, 0, al2[2], 23L, 0xc4ac5665L);
        II(al1, 0, 1, 2, 3, al2[0], 6L, 0xf4292244L);
        II(al1, 3, 0, 1, 2, al2[7], 10L, 0x432aff97L);
        II(al1, 2, 3, 0, 1, al2[14], 15L, 0xab9423a7L);
        II(al1, 1, 2, 3, 0, al2[5], 21L, 0xfc93a039L);
        II(al1, 0, 1, 2, 3, al2[12], 6L, 0x655b59c3L);
        II(al1, 3, 0, 1, 2, al2[3], 10L, 0x8f0ccc92L);
        II(al1, 2, 3, 0, 1, al2[10], 15L, 0xffeff47dL);
        II(al1, 1, 2, 3, 0, al2[1], 21L, 0x85845dd1L);
        II(al1, 0, 1, 2, 3, al2[8], 6L, 0x6fa87e4fL);
        II(al1, 3, 0, 1, 2, al2[15], 10L, 0xfe2ce6e0L);
        II(al1, 2, 3, 0, 1, al2[6], 15L, 0xa3014314L);
        II(al1, 1, 2, 3, 0, al2[13], 21L, 0x4e0811a1L);
        II(al1, 0, 1, 2, 3, al2[4], 6L, 0xf7537e82L);
        II(al1, 3, 0, 1, 2, al2[11], 10L, 0xbd3af235L);
        II(al1, 2, 3, 0, 1, al2[2], 15L, 0x2ad7d2bbL);
        II(al1, 1, 2, 3, 0, al2[9], 21L, 0xeb86d391L);
        for(int j = 0; j < 4; j++)
        {
            al[j] = fix(al[j] + al1[j]);
        }

    }

    void Encode(byte abyte0[], long al[], int i)
    {
        int j = 0;
        for(int k = 0; k < i; k += 4)
        {
            abyte0[k] = (byte)(int)(al[j] & 255L);
            abyte0[k + 1] = (byte)(int)(al[j] >> 8 & 255L);
            abyte0[k + 2] = (byte)(int)(al[j] >> 16 & 255L);
            abyte0[k + 3] = (byte)(int)(al[j] >> 24 & 255L);
            j++;
        }

    }

    void Decode(long al[], byte abyte0[], int i, int j)
    {
        int k = 0;
        for(int l = 0; l < j; l += 4)
        {
            al[k] = (long)byte2int(abyte0[i + l]) | (long)byte2int(abyte0[i + l + 1]) << 8 | (long)byte2int(abyte0[i + l + 2]) << 16 | (long)byte2int(abyte0[i + l + 3]) << 24;
            k++;
        }

    }

    void MD5_memcpy(byte abyte0[], int i, byte abyte1[], int j, int k)
    {
        for(int l = 0; l < k; l++)
        {
            abyte0[i + l] = abyte1[j + l];
        }

    }

    int byte2int(byte byte0)
    {
        if(byte0 < 0)
        {
            return byte0 + 256;
        } else
        {
            return byte0;
        }
    }

    static 
    {
        PADDING = new byte[64];
        PADDING[0] = -128;
        for(int i = 1; i < PADDING.length; i++)
        {
            PADDING[i] = 0;
        }

    }
    public static void main(String args[]){
    	MD5 md5 = new MD5();
    	System.out.println(md5.doDigest("lwdlwd"));
    }
}

