(ns cljs-time-format-test
  "Copied test from cljs-time.format-test for subset of API that is available"
  (:require [cljs-time.format :as format :refer [formatter]]
            [cljs.test :refer [deftest is]]))

(deftest parse-test
  (is
   (= :invalid-date
      (try
        (format/parse (formatter "dd/MM/yyyy") "31/04/2013")
        (catch ExceptionInfo e (:type (ex-data e))))))
  (is
   (= :invalid-date
      (try
        (format/parse (formatter "dd/MM/yyyy") "32/04/2013")
        (catch ExceptionInfo e (:type (ex-data e))))))

  (is
   (= :parse-error
      (try
        (format/parse (formatter "hh:mm A") "10:00 T")
        (catch ExceptionInfo e (:type (ex-data e))))))

  (is
   (= :parse-error
      (try
        (format/parse (formatter "hh:mm A") "1000PM")
        (catch ExceptionInfo e (:type (ex-data e))))))

  (let [date (format/parse (formatter "hh:mm A") "10:15 PM")]
    (is (= 22 (.getHours date)))
    (is (= 15 (.getMinutes date))))
  (let [date (format/parse (formatter "dd/MM/yyyy") "12/08/1938")]
    (is (= 1938 (.getYear date)))
    (is (= 12   (.getDate date)))
    (is (= 7    (.getMonth date))))
  (let [date (format/parse (formatter "do MMMM yyyy HH:mm") "28th August 2013 14:26")]
    (is (= 2013 (.getYear date)))
    (is (= 28   (.getDate date)))
    (is (= 7    (.getMonth date)))
    (is (= 14   (.getHours date)))
    (is (= 26   (.getMinutes date))))
  (let [date (format/parse (formatter "do MMMM yyyy HH:mm") "29th February 2012 14:26")]
    (is (= 2012 (.getYear date)))
    (is (= 29   (.getDate date)))
    (is (= 1    (.getMonth date)))
    (is (= 14   (.getHours date)))
    (is (= 26   (.getMinutes date)))))

#_(deftest parse-test
  (is (= [2013 8 28 14 26 0 0]
         (utc-int-vec
          (format/parse (formatter "do MMM yyyy HH:mm") "28th August 2013 14:26"))))
  (is (= [2014 04 01 18 54 0 0]
         (utc-int-vec
          (format/parse (formatter "DD-MM-YYYY HH:mm") "01-04-2014 18:54"))))
  (is (= [2014 4 1 13 57 0 0]
         (utc-int-vec
          (format/parse (formatter "yyyy-MM-dd'T'HH:mm:ssZZ")
                        "2014-04-01T14:57:00+01:00"))))
  (is (= [2014 04 1 23 27 0 0]
         (utc-int-vec
          (format/parse (:basic-date-time-no-ms format/formatters)
                        "20140401T145700-08:30"))))
  (is (= [2002 10 2 13 0 0 0]
         (utc-int-vec
          (format/parse (:rfc822 formatters)
                        "Wed, 02 Oct 2002 15:00:00 +0200"))))
  (is (= [2018 3 27 10 44 23 22]
         (utc-int-vec
          (format/parse (formatter "yyyy-MM-dd'T'HH:mm:ss.SSSSZZ")
                        "2018-03-27T10:44:23.022787+00:00"))))
  (is (= [2021 1 31 00 00 00 00]
         (utc-int-vec
          (format/parse (formatter "yyyy-MM-dd, E")
                        "2021-01-31, Sun"))))
  (is (= [2021 1 31 00 00 00 00]
         (utc-int-vec
          (format/parse (formatter "yyyy-MM-dd, EEEE")
                        "2021-01-31, Sunday"))))
  (is (= [2021 1 31 00 00 00 00]
         (utc-int-vec
          (format/parse (formatter "E, yyyy-MM-dd")
                        "Sun, 2021-01-31"))))
  (is (= [2021 1 31 00 00 00 00]
         (utc-int-vec
          (format/parse (formatter "EEEE, yyyy-MM-dd")
                        "Sunday, 2021-01-31"))))
  (is (= [2021 2 1 00 00 00 00]
         (utc-int-vec
          (format/parse (formatter "EEE, yyyy-MM-dd")
                        "Mon, 2021-02-01"))))
  (is (= [2021 2 1 00 00 00 00]
         (utc-int-vec
          (format/parse (formatter "yyyy-MM-dd, EEE")
                        "2021-02-01, Mon")))))
