(ns incanter_processing_example.core)

(use '(incanter core processing))

;;go board uses incanter processing library

;;change to atom w/ piece-vector inside
(def piece-vector [:black :white :empty :empty :black :black :white :empty :black :white :white :empty :white :empty :empty :empty :white :empty :black :white :white :empty :white :empty :empty :empty :empty :white :empty :black :white :white :empty :empty :empty :empty :white :empty :black :white :empty :empty :empty :empty :white :empty :black :white :empty :empty :empty :empty :white :empty :black :white :empty :empty :empty :empty :white :empty :black :white :black :white :empty :empty :black :black :white :empty :black :white :white :empty :white :empty :empty :empty :white :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white])
;;(def piece-vector [:black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black :black])
;;(def piece-vector [:white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white :white])


(def board-size 8)
(def board-full (+ (* 50 (- board-size 1)) 125))


;;this has a go board that takes in a vector of all the spaces and draws a board based on that input
;;it has a space to include captured pieces, though currently elim-blk and elim-white are created in let

(let [radius (ref 50.0)
      X (ref nil)
      Y (ref nil)
      nX (ref nil)
      nY (ref nil)
      elim-blk 0
      elim-white 2
      
      delay 16
      
      ;; define a sketch object (i.e. PApplet)
      sktch (sketch
              
              ;; define the setup function
              (setup []
                     (doto this
                       ;no-loop
                       (size board-full board-full)
                       (no-stroke)
                       (framerate 15)
                       (background 125)
                       smooth)
                     (dosync
                       (ref-set X (/ (width this) 2))
                       (ref-set Y (/ (width this) 2))
                       (ref-set nX @X)
                       (ref-set nY @Y)
                       (text-font this (create-font this "Verdana" 32))))
              
              ;; define the draw function
              (draw []
                    (dosync
                      (ref-set radius (+ @radius (sin (/ (frame-count this) 4))))
                      (ref-set X (+ @X (/ (- @nX @X) delay)))
                      (ref-set Y (+ @Y (/ (- @nY @Y) delay))))
                    (doto this
                      ;;draw-go-board
                      (rect 0 0 board-full board-full )
                      (fill 255)
                      (stroke 20)
                      (rect 0 0 (+ (* 50 board-size) 50) (+ (* 50 board-size) 50))
                      (stroke 8)
                      (with-translation 
                        [50 50]
                        ;; add game board rectangles and pieces if piece at location
                        (dotimes [i board-size] 
                          (dotimes [j board-size]
                            (let [piece-color (nth piece-vector (+ (* i board-size) j))]
                              (fill this 255 255 255)
                              (when (and (< i (- board-size 1) ) (< j (- board-size 1))) 
                                (rect this (* i 50) (* j 50) 50 50) ;;draw tiles
                                ) 
                              ;;if piece present, draw it
                              (when
                                (not (= :empty piece-color)) 
                                (do 
                                  (if (= piece-color :white) 
                                    (fill this 255)
                                    (fill this 0))
                                  (ellipse this (* i 50) (* j 50) 15 15)
                                  )
                                ) 
                              ))))
                      ;;end draw-go-board
                      
                      
                      ;;draw-eliminated
                      ;; text align ints - 37, 39, 3s
                      
                      (ellipse 50 (+ (* 50 (- board-size 1)) 75) 10 10)
                      (fill 0)
                      (text-align 37)
                      (text elim-blk 60 (+ (* 50 (- board-size 1) ) 87))
                      (ellipse (- board-full 75) (+ (* 50 (- board-size 1)) 75) 10 10)
                      (text-align 39)
                      (text elim-white (- board-full 85) (+ (* 50 (- board-size 1)) 87))))
              
              ;;end draw-eliminated
              
              ;; define mouseMoved function (mouseMoved and mouseDraw 
              ;; require a 'mouse-event' argument unlike the standard Processing 
              ;; methods)
              (mouseMoved [mouse-event]
                          (dosync
                         ;;mouse event not used yet
                            (ref-set nX (mouse-x mouse-event)) 
                            (ref-set nY (mouse-y mouse-event)))))]
  
  ;; use the view function to display the sketch
  (view sktch :size [board-full board-full]))
