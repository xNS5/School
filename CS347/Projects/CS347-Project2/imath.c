#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <sys/time.h>
#include <pthread.h>
#include <string.h>

#define THREADS 4

#define filterWidth 3
#define filterHeight 3

#define RGB_MAX 255

pthread_mutex_t mutex;

typedef struct {
	 unsigned char r, g, b;
} PPMPixel;

struct parameter {
	PPMPixel *image;         //original image
	PPMPixel *result;        //filtered image
	unsigned long int w;     //width of image
	unsigned long int h;     //height of image
	unsigned long int start; //starting point of work
	unsigned long int size;  //equal share of work (almost equal if odd)
};

// color_boundary thresholds a given color value
// to the range [0,255]
int color_boundary(int color) {
    if (color > RGB_MAX) {
        color = RGB_MAX;
    }
    if (color < 0) {
        color = 0;
    }
    return color;
}

/*This is the thread function. It will compute the new values for the region of image specified in params (start to start+size) using convolution.
    (1) For each pixel in the input image, the filter is conceptually placed on top of the image with its origin lying on that pixel.
    (2) The  values  of  each  input  image  pixel  under  the  mask  are  multiplied  by the corresponding filter values.
    (3) The results are summed together to yield a single output value that is placed in the output image at the location of the pixel being processed on the input.
 */
void *threadfn(void *param)
{
    //Locking with a mutex
    pthread_mutex_lock(&mutex);
    if(param == NULL){
        pthread_exit(NULL);
    }
    // convert params back to correct data type
    struct parameter *params = param;

    // edge detection filter
    int laplacian[filterWidth][filterHeight] =
	{
	  -1, -1, -1,
	  -1,  8, -1,
	  -1, -1, -1,
	};

    /*For all pixels in the work region of image (from start to start+size)
      Multiply every value of the filter with corresponding image pixel. Note: this is NOT matrix multiplication.
      Store the new values of r,g,b in p->result.
     */
    for(int i = 0; i < params->w; i++){ // iterate over width
        for(int j = params->start; j < params->start+params->size; j++){ // iterate over chunk of height
            int red = 0, green = 0, blue = 0; // new values
            for(int k = 0; k < filterWidth; k++){ // iter over filter width
                for(int l = 0; l < filterHeight; l++){ // iter over filter height
                    int x = (i - (filterWidth/2) + k + params->w) % params->w; // convolution
                    int y = (j - (filterHeight/2) + l + params->h) % params->h; //convolution
                    // new values
                    red += ((int)params->image[(y*params->w + x)].r) * laplacian[l][k];
                    green += ((int)params->image[(y*params->w + x)].g) * laplacian[l][k];
                    blue += ((int)params->image[(y*params->w + x)].b) * laplacian[l][k];
                }
            }
            // thresholding
            red = color_boundary(red);
            green = color_boundary(green);
            blue = color_boundary(blue);
            // assign new values
            params->result[j*params->w + i].r = red;
            params->result[j*params->w + i].g = green;
            params->result[j*params->w + i].b = blue;
        }
    }
    //Unlocking mutex
    pthread_mutex_unlock(&mutex);
    pthread_exit(NULL);
}
/*Create a new P6 file to save the filtered image in. Write the header block
 e.g. P6
      Width Height
      Max color value
 then write the image data.
 The name of the new file shall be "name" (the second argument).
 */
void writeImage(PPMPixel *image, char *name, unsigned long int width, unsigned long int height)
{
    FILE *file; // pointer to output file
    file = fopen(name, "wb");
    // file read error
    if (!file) {
        fprintf(stderr, "Unable to open %s for writing.\n", name);
        fclose(file);
        free(image);
        exit(1);
    }

    // writing constants
    fprintf(file, "%s\n", "P6"); //File Format
    fprintf(file, "%s\n", "# Created by: Josh Myers-Dean and Michael Kennedy"); // comments
    fprintf(file, "%lu %lu\n", width, height); // write image dimensions
    fprintf(file, "%d\n", RGB_MAX); // write max RGB value

    // write RGB values from pixel array to file
    // make sure height triplets were written
    if (fwrite(image, 3*width, height, file) != height) {
        fprintf(stderr,"Error writing RGB values to %s.\n", name);
        fclose(file);
        free(image);
        exit(1);
    }

    fprintf(file,"\n"); // add new line char to EOF
    // garbage collection
    fclose(file);
}

/* Open the filename image for reading, and parse it.
    Example of a ppm header:    //http://netpbm.sourceforge.net/doc/ppm.html
    P6                  -- image format
    # comment           -- comment lines begin with
    ## another comment  -- any number of comment lines
    200 300             -- image width & height
    255                 -- max color value
 
 Check if the image format is P6. If not, print invalid format error message.
 Read the image size information and store them in width and height.
 Check the rgb component, if not 255, display error message.
 Return: pointer to PPMPixel that has the pixel data of the input image (filename)
 */
PPMPixel *readImage(const char *filename, unsigned long int *width, unsigned long int *height)
{
    PPMPixel *img; // return var
    char buf[4]; // buffer to read file info, 4 seems to be the minimum needed
    FILE *file; // pointer to file
    int char_reader; // comment checker
    int max_val; // max value
    size_t bytes; // number of bytes to allocate memory

    // open file and check for error
    file = fopen(filename, "rb");
    if (!file) {
        fprintf(stderr, "Error opening %s\n", filename);
        exit(1);
    }

    //get first 4 bytes of file
    if (!fgets(buf, sizeof(buf), file)) {
        fprintf(stderr, "Error reading %s\n", filename);
        fclose(file);
        exit(1);
    }

    // check for correct format
    if (buf[0] != 'P' || buf[1] != '6') {
        fprintf(stderr, "Error: image format must be P6.\n");
        fclose(file);
        exit(1);
    }

    // check for comments
    while ((char_reader = fgetc(file)) == '#'){
        // clear rest of line
        while ((char_reader = fgetc(file)) != '\n') {
            continue;
        }
    }
    ungetc(char_reader,file); // unconsume the char just in case no comments

    // get height and width of image
    if (fscanf(file, "%lu %lu", width, height) != 2) {
        fprintf(stderr, "Image dimensions not found for %s\n", filename);
        fclose(file);
        exit(1);
    }

    // clear line
    while (fgetc(file) != '\n') {
        continue;
    }
    // number of bytes for malloc
    // width * height * 3 channels
    bytes = (*width) * (*height) * 3;

    // check for max value in file and compare it to the max allowed value
    if (fscanf(file, "%d", &max_val) != 1) {
        fprintf(stderr, "Image max value not found for %s", filename);
        fclose(file);
        exit(1);
    }

    if (max_val != RGB_MAX) {
        fprintf(stderr, "Max value larger not equal to 255.\n");
        fclose(file);
        exit(1);
    }

    // clear line
    while (fgetc(file) != '\n') {
        continue;
    }

    // allocate memory for image
    img = (PPMPixel*) malloc(bytes);

    // memory allocation failed
    if (!img) {
        fprintf(stderr, "Unable to allocate memory for %s.\n", filename);
        fclose(file);
        free(img);
        exit(1);
    }

    // read in the RGB values to our array of pixels
    // make sure height bytes are read
    if (fread(img, 3*(*width), (*height), file) != (*height)) {
        fprintf(stderr, "Error reading %s\n", filename);
        fclose(file);
        free(img);
        exit(1);
    }

    // gabage collection
    fclose(file);
    return img;
}

/* Create threads and apply filter to image.
 Each thread shall do an equal share of the work, i.e. work=height/number of threads.
 Compute the elapsed time and store it in *elapsedTime (Read about gettimeofday).
 Return: result (filtered image)
 */
PPMPixel *apply_filters(PPMPixel *image, unsigned long w, unsigned long h, double *elapsedTime) {

    PPMPixel *result;
    //allocate memory for result
    result = malloc(3*w*h);

    //allocate memory for parameters (one for each thread)
    unsigned long work = h/THREADS;

    /*create threads and apply filter.
     For each thread, compute where to start its work.  Determine the size of the work. If the size is not even, the last thread shall take the rest of the work.
     */
    struct timeval start_time, completed_time;

    // create array of threads
    pthread_t thread_id[THREADS];
    int ret;


    // iterate over threads
    gettimeofday(&start_time, NULL);
    for(int i = 0; i < THREADS; i++){
        // create param object of struct
        struct parameter *params;
        params = malloc(sizeof(struct parameter));
        params->image = image;
        params->result = result;
        params->w = w;
        params->h = h;

        //calculate start and end of work
        unsigned long start = work * i, size = 0;
        if(i == THREADS-1){
            size = h-start;
        } else {
            size = work;
        }
        params->start = start;
        params->size = size;

        // check for thread errors
        ret = pthread_create(&thread_id[i], NULL, threadfn, (void*)params);
        if(ret){
            fprintf(stderr, "Error Generating Thread: %i\r\n", i);
        }
    }
   //Let threads wait till they all finish their work.
   for(int i = 0; i < THREADS; i++){
       ret = pthread_join(thread_id[i], NULL);
       if(ret){
           fprintf(stderr, "Error Joining Thread: %i\r\n", i);
       }
   }
   // Calculating time
   gettimeofday(&completed_time, NULL);
   *elapsedTime = completed_time.tv_sec - start_time.tv_sec;
   *elapsedTime += (completed_time.tv_usec - start_time.tv_usec)/1000000.0;
   return result;
}

/*The driver of the program. Check for the correct number of arguments. If wrong print the message: "Usage ./a.out filename"
    Read the image that is passed as an argument at runtime. Apply the filter. Print elapsed time in .3 precision (e.g. 0.006 s). Save the result image in a file called laplacian.ppm. Free allocated memory.
 */
int main(int argc, char *argv[])
{
    // initial vars
    unsigned long int w, h;
    double elapsedTime = 0.0;

    // error handling
    if (argc != 2) {
        fprintf(stderr,"Usage: ./imath filename\n");
        return(1);
    }

    // get filename
    char *filename = argv[1];
    char *return_filename = "laplacian.ppm";
    PPMPixel *img;

    // read in image
    img = readImage(filename, &w, &h);

    //Applying filter to image
    img = apply_filters(img, w, h, &elapsedTime);
    printf("Threads: %d\r\nElapsed Time: %.3f\r\n", THREADS, elapsedTime);
    writeImage(img, return_filename, w, h);

    // free allocated memory
    free(img);

    return 0;
}
