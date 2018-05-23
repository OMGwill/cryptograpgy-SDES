

public class SDES
{
   static String[][] s1Box = {
        
        {"101","010","001","110","011","100","111","000"},
        {"001","100","110","010","000","111","101","011"}
    };
    
   static String[][] s2Box = {
        
        {"100","000","110","101","111","001","011","010"},
        {"101","011","000","111","110","010","001","100"}
    };
    
    public static int[] encrypt(int[] ptext, int[] k, int round){
        int[] L = new int[6];
        int[] R = new int[6];
        int[] L2 = new int[6];
        int[] R2 = new int[6];
        int[] E = new int[8];
        int[] key = new int[8];
        int[] F = new int[6];
        int[] ctext = new int[12];
        
        
        //split array into left and right halves
        for (int i = 0; i < ptext.length/2; i++){
            L[i] = ptext[i];
        }
        
        
        for (int i = ptext.length/2; i < ptext.length ; i++){
            R[i-ptext.length/2] = ptext[i]; 
        }
        
     
        
        //l1 = r0
        
        L2 = R.clone();
        
   
        
        //expansion array
        E[0] = R[0];
        E[1] = R[1];
        E[2] = R[3];
        E[3] = R[2];
        E[4] = R[3];
        E[5] = R[2];
        E[6] = R[4];
        E[7] = R[5];
        
        
       
        
        //compute correct key
        int j = round - 1;
        for(int i = 0; i < 8; i++){
            
            key[i] = k[j++];
            
            if(j == 8){
                j = 0;
            }
        }
        

        
        //expansion XOR with key
        for(int i = 0; i < E.length; i++){
            E[i] ^= key[i];
        }
        
        
        
       //compute sbox column
        String s1col = "";
        String s2col = "";
        
        for(int i = 1; i < E.length/2 ; i++){
            s1col += Integer.toString(E[i]);
        }
        
        for(int i = 5; i < E.length ; i++){
            s2col += Integer.toString(E[i]);
        }
       
        //get sbox values
        String s1 = s1Box[E[0]][Integer.parseInt(s1col,2)];
        String s2 = s2Box[E[4]][Integer.parseInt(s2col,2)];
        
        
        //input sbox values in array
        for(int i = 0; i < F.length; i++){
            if(i < 3){
                String s = ""+s1.charAt(i);
                F[i]= Integer.parseInt(s);
            }
            else{
                String s = ""+s2.charAt(i- (F.length/2));
                F[i] = Integer.parseInt(s);
            }
        }
        
      
        
        //calculate r2
         for(int i = 0; i < R2.length; i++){
            R2[i] = L[i] ^ F[i]; 
        }
        
     
        //concatenate L2 and R2
        for(int i = 0; i < ctext.length; i++){
            if(i < ctext.length/2){
                ctext[i] = L2[i];
            }else{
                ctext[i] = R2[i - ctext.length/2];
            }
        }
        
       
        return ctext;
    }
    
     public static int[] decrypt(int[] ctext, int[] k, int round){
        int[] L = new int[6];
        int[] R = new int[6];
        int[] L2 = new int[6];
        int[] R2 = new int[6];
        int[] E = new int[8];
        int[] key = new int[8];
        int[] F = new int[6];
        int[] ptext = new int[12];
        
        
        //split array into left and right halves
        for (int i = 0; i < ctext.length/2; i++){
            L2[i] = ctext[i];
        }
        
        
        for (int i = ctext.length/2; i < ctext.length ; i++){
            R2[i-ctext.length/2] = ctext[i]; 
        }
        
     
        
        //R = L2
        
        R = L2.clone();
        
        //expansion array
        E[0] = R[0];
        E[1] = R[1];
        E[2] = R[3];
        E[3] = R[2];
        E[4] = R[3];
        E[5] = R[2];
        E[6] = R[4];
        E[7] = R[5];
        
        
        //compute correct key
        int j = round - 1;
        for(int i = 0; i < 8; i++){
            
            key[i] = k[j++];
            
            if(j == 8){
                j = 0;
            }
        }
        

        
        //expansion XOR with key
        for(int i = 0; i < E.length; i++){
            E[i] ^= key[i];
        }
        
        
        //compute sbox column
        String s1col = "";
        String s2col = "";
        
        for(int i = 1; i < E.length/2 ; i++){
            s1col += Integer.toString(E[i]);
        }
        
        for(int i = 5; i < E.length ; i++){
            s2col += Integer.toString(E[i]);
        }
       
        //get sbox values
        String s1 = s1Box[E[0]][Integer.parseInt(s1col,2)];
        String s2 = s2Box[E[4]][Integer.parseInt(s2col,2)];
        
        
        //input sbox values in array
        for(int i = 0; i < F.length; i++){
            if(i < 3){
                String s = ""+s1.charAt(i);
                F[i]= Integer.parseInt(s);
            }
            else{
                String s = ""+s2.charAt(i- (F.length/2));
                F[i] = Integer.parseInt(s);
            }
        }
        
        
        //calculate L
         for(int i = 0; i < L.length; i++){
            L[i] = R2[i] ^ F[i]; 
        }
        
         //concatenate L and R
        for(int i = 0; i < ptext.length; i++){
            if(i < ptext.length/2){
                ptext[i] = L[i];
            }else{
                ptext[i] = R[i - ptext.length/2];
            }
        }
        
        return ptext;
        }
    
    public static void print(int[]a, int r){
        System.out.println("Round " + r +": ");
        
        System.out.print("L" + r + " ");
        for(int i = 0; i < a.length/2; i++)
        {
            System.out.print(a[i]);
        }
        
        System.out.print("\tR" + r + " ");
        for(int i = a.length/2; i < a.length; i++)
        {
            System.out.print(a[i]);
        }
        System.out.println();
    }
    
   public static void main(){
      
      int[] plaintext = {1,0,0,0,1,0,1,1,0,1,0,1};
      int[] key = {1,1,1,0,0,0,1,1,1};
      
      System.out.println("Encryption");
      
      print(plaintext,0);
      
      int[] ciphertext = encrypt(plaintext,key,1);
      print(ciphertext,1);
      
      int[] ciphertext2 = encrypt(ciphertext,key,2);
      print(ciphertext2,2);
      
      int[] ciphertext3 = encrypt(ciphertext2,key,3);
      print(ciphertext3,3);
      
      int[] ciphertext4 = encrypt(ciphertext3,key,4);
      print(ciphertext4,4);
      
      //////////////////////////////////////////////////////
      System.out.println("\n\nDecryption");
      
      print(ciphertext4,4);
      
      int[] plaintext4 = decrypt(ciphertext4,key,4);
      print(plaintext4,3);
      
      int[] plaintext3 = decrypt(plaintext4,key,3);
      print(plaintext3,2);
      
      int[] plaintext2 = decrypt(plaintext3,key,2);
      print(plaintext2,1);
      
      int[] plaintext1 = decrypt(plaintext2,key,1);
      print(plaintext1,0);
      
    }
    
    
}
