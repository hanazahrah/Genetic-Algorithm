/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package ga_1301174392;

import java.util.Random;

/**
 *
 * @author hana
 */
public class GA_1301174392 {

    static Random rand = new Random();
    
    //Inisiasi populasi
    static int[][] initPop(int popSize){
        int[][] population = new int[popSize][6];
        
        for (int i = 0; i < popSize; i++){
            for (int j = 0; j < 6; j++) {
                population[i][j] = rand.nextInt(2);  
            }
        }
        return population;
    }
    
    //Print population
    static void print(int[][] population){
        for (int i = 0; i < population.length; i++) {
            for (int j = 0; j < population.length; j++) {
                System.out.print(population[i][j]);
                
            }
             System.out.println("");
        }
    }
    
    //Decode chromosome
    static double[] decode(int[] k){
        int max1 = 3;
        int min1 = -3;
        int max2 = 2;
        int min2 = -2;
        double x[] = new double[2];
        double pengali1 = (max1-min1)/((Math.pow(2,-1)+Math.pow(2,-2)+ Math.pow(2,-3)));
        double pengali2 = (max2-min2)/((Math.pow(2,-1)+Math.pow(2,-2)+ Math.pow(2,-3)));
        x[0] = min1 + pengali1*(k[0]*Math.pow(2, -1) + k[1]*Math.pow(2, -2) + k[2]*Math.pow(2,-3));
        x[1] = min2 + pengali2*(k[3]*Math.pow(2, -1) + k[4]*Math.pow(2, -2) + k[5]*Math.pow(2,-3));
        return x;
    }
    
    //Objective function
    //Properties of a solution represented by chromosome
    static double objective(double[] x){
        double x1 = x[0];
        double x2 = x[1];
        //rumus :
        double objektif = (4 - 2.1 * Math.pow(x1, 2) + Math.pow(x1, 4) /3) * Math.pow(x1, 2)+ (x1*x2) + ((-4 + Math.pow(x2,2)) * Math.pow(x2,2));
        return  objektif;
    }
    
    static double fitness(double obj){
        return -obj;
    }
    
    //Calculate fitness of each chromosome in one population
    static double[] calculateFitness(int[][] population){
        double[] fitness = new double[population.length];
        for (int i = 0; i < fitness.length; i++) {
            fitness[i] = fitness(objective(decode(population[i])));
        }
        return fitness;
    }
    
    //Parent selection by roulette wheel
    static int[][] roulettewheel(int[][] population, double[] fitness){
        double[] proportion = fitness.clone();
        int[][] parent = new int[2][];
        double total = 0;
        //sum all fitness values
        for (double d : fitness) {
            total += d;
        }
        //define a value of proportion for each chromosome
        for (int i = 0; i < proportion.length; i++) {
            proportion[i] = fitness[i]/total;
            
        }
        //define parent
        for (int i = 0; i < 2; i++) {
            double r = rand.nextDouble();
            double tot = 0;
            int j = 0;
            while (tot<r) {                
                tot += proportion[j];
                j++;
            }
            parent[i] = population[j-1];
        }
        return parent; 
    }
    
    //Crossover parent to get offspring
    static int[][] crossover(int[][] parent, double pCrossover){
        double r = rand.nextDouble();
        if (r < pCrossover) {
            int[][] child = parent.clone();
            int line = rand.nextInt(6);
            for (int i = line; i < 6; i++) {
                child[0][i] = parent[1][i];
                child[1][i] = parent[0][i];
            }
            return child;
        }
        return parent;
    }
    
    //Child mutation
    static int[] mutation(int[] child, double pMutation){
        for (int i = 0; i < child.length; i++) {
            double r = rand.nextDouble();
            if (r < pMutation) {
                child[i] = rand.nextInt(2);
            }
        }
        return child;
    }
    
    //Survivor selection by steady state fitness-based
    static int[][] steadyState(int[][] population, double[] fitness, int[][] child){
        
        for (int i = 0; i < child.length; i++) {
            double minfitness = fitness[0];
            int n = 0;
            for (int j = 0; j < fitness.length; j++) {
                if(minfitness > fitness[j]){
                    n = j;
                    minfitness = fitness[j];
                }
            }
            population[n] = child[i].clone();
            
        }
        return population;
    }
    
    
    public static void main(String[] args) {
       
       //Population size 
       int popSize = 1000;
       //Maximum generation
       int maxGen = 3000;
       //Probability crossover
       double Pc = 0.8;
       //Probability mutation
       double Pm = 0.5;
       //inisiasi populasi
       int[][] population = initPop(popSize);
       double[] fitness;
       
       int gen = 0;
       //Looping generation
       while(gen < maxGen){
           fitness = calculateFitness(population);
            int[][] parent = roulettewheel(population, fitness);
            int[][] child = crossover(parent, Pc);
            //Child mutation
            int[][] childm = child.clone();
            childm[0] = mutation(child[0], Pm).clone();
            childm[1] = mutation(child[1], Pm).clone();
            
            population = steadyState(population, fitness, childm);
             
           gen++;
       }
       
       //looping generasi selesai
       //find best fitness of all chromosome in last generation
       fitness = calculateFitness(population);
        double max = fitness[0];
            int id = 0;
            for (int i = 0; i < fitness.length; i++) {
                if (max < fitness[i]) {
                    id = i;
                    max = fitness[i];
                }
            }
        
        //BEST INDIVIDU
            double[] xy = decode(population[id]);
            System.out.println("Individu terbaik : ");
            System.out.println("Kromosom ke : "+id);
            System.out.println("X1 : "+xy[0]);
            System.out.println("X2 : "+xy[1]);
            System.out.println("hasil minimum : "+(-max));
       
      
    }
    
}
