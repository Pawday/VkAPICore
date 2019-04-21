package per.pawday.vkbot.json;


import java.io.*;
import java.nio.charset.StandardCharsets;


public class JsonFormatter
{

    private static StringBuilder formatToBuilder(InputStream stream, String separator) throws IOException
    {


        InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);


        StringBuilder builder = new StringBuilder();


        int indent = 0;

        boolean inString = false;

        char c;
        int by;

        while ((by = reader.read()) != -1)
        {
            c = (char) by;
            if (!inString)
            {
                switch (c) {
                    case '{':
                    case '[':

                        if (builder.length() != 0){
                            builder.append('\n');
                            for (int i = 0;i < indent;i++) {
                                builder.append(separator);
                            }
                        }

                        builder.append(c).append('\n');
                        indent++;
                        for (int i = 0;i < indent;i++) {
                            builder.append(separator);
                        }

                        break;

                    case '}':
                    case ']':

                        indent--;
                        builder.append('\n');
                        for (int i = 0;i < indent;i++) {
                            builder.append(separator);
                        }
                        builder.append(c);
                        break;


                    case ',':

                        builder.append(',').append('\n');
                        for (int i = 0;i < indent;i++) {
                            builder.append(separator);
                        }


                        break;


                    case ':':
                        builder.append(' ').append(':').append(' ');

                        break;

                    case '\t':
                    case ' ':
                    case '\n':
                    case '\r':

                        break;

                    case '"':
                        inString = true;
                        builder.append('"');
                        break;

                    default:
                        builder.append(c);

                }
            }
            else if (c == '"')
            {
                inString = false;
                builder.append('"');
            } else builder.append(c);



        }

        return builder;
    }





    public static StringBuilder formatToStringBuilder(InputStream stream,String separator) throws IOException
    {
        return formatToBuilder(stream,separator);
    }

    public static StringBuilder formatToStringBuilder(String string,String separator) throws IOException
    {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        return formatToBuilder(new ByteInputStream(bytes),separator);
    }

    public static StringBuilder formatToStringBuilder(final Reader reader, String separator) throws IOException
    {
        return formatToBuilder(new InputStream()
        {
            @Override
            public int read() throws IOException
            {
                return reader.read();
            }
        }, separator);
    }

    public static String formatToString(InputStream stream,String separator) throws IOException
    {
        return formatToBuilder(stream,separator).toString();
    }

    public static String formatToString(String string,String separator) throws IOException
    {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        return formatToBuilder(new ByteInputStream(bytes),separator).toString();
    }

    public static String formatToString(final Reader reader, String separator) throws IOException
    {
        return formatToBuilder(new InputStream()
        {
            @Override
            public int read() throws IOException
            {
                return reader.read();
            }
        },separator).toString();
    }



    public static InputStream formatToInputStream(InputStream stream,String separator) throws IOException
    {
        StringBuilder result =  formatToBuilder(stream,separator);
        return new CharsInputStream(result);
    }

    public static InputStream formatToInputStream(String string,String separator) throws IOException
    {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        StringBuilder result = formatToBuilder(new ByteInputStream(bytes),separator);
        return new CharsInputStream(result);
    }

    public static InputStream formatToInputStream(final Reader reader, String separator) throws IOException
    {
        StringBuilder result = formatToBuilder(new InputStream()
        {
            @Override
            public int read() throws IOException
            {
                return reader.read();
            }
        },separator);
        return new CharsInputStream(result);
    }


    private static class ByteInputStream extends InputStream
    {
        private byte[] bytes;
        private int iter = 0;

        public ByteInputStream(byte[] bytes)
        {
            this.bytes = bytes;
        }

        @Override
        public int read() throws IOException
        {
            if (iter == 0)
            {
                iter++;
                return bytes[0];
            }
            else
            {
                if (iter == bytes.length)
                {
                    return -1;
                }
                else
                {
                    iter++;
                    return bytes[iter - 1];
                }
            }
        }
    }


    private static class CharsInputStream extends InputStream
    {
        private StringBuilder builder;
        private int length;
        private int pos;

        private CharsInputStream(StringBuilder builder)
        {
            this.builder = builder;
            this.length = builder.length();
            this.pos = 0;
        }


        @Override
        public int read()
        {

            if ( this.pos == this.length )
            {
                return -1;
            }

            this.pos++;


            return this.builder.charAt(this.pos - 1);
        }
    }


}