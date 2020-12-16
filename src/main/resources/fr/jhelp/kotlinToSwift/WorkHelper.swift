import Foundation

public typealias Long = Int64

public typealias Byte = UInt8

public extension String
{
    var length : Int { get { return self.count } }

    func isEmpty() -> Bool
    {
       return self.isEmpty
    }

    func isNotEmpty() -> Bool
    {
       return !self.isEmpty
    }

    func toInt() throws -> Int
    {
        let value = Int(self)

        guard value != nil else { throw GenericError("Can not convert \(self) to integer") }

        return value!
    }

    func toLong() throws -> Long
    {
        let value = Long(self)

        guard value != nil else { throw GenericError("Can not convert \(self) to long") }

        return value!
    }

    func toFloat() throws -> Float
    {
        let value = Float(self)

        guard value != nil else { throw GenericError("Can not convert \(self) to float") }

        return value!
    }

    func toDouble() throws -> Double
    {
        let value = Double(self)

        guard value != nil else { throw GenericError("Can not convert \(self) to double") }

        return value!
    }

    func startsWith(_ start:String) -> Bool
    {
        return self.hasPrefix(start)
    }

    func indexOf(_ searched:String) -> Int
    {
        return self.indexOf(searched, 0)
    }

    func indexOf(_ searched:String, _ index:Int) -> Int
    {
        if(searched.isEmpty)
        {
            return index
        }

        let selfArray = Array(self.unicodeScalars)
        let searchedArray = Array(searched.unicodeScalars)
        let limit = selfArray.count - searchedArray.count + 1
        var index = index
        var check = 0
        var look = 0

        while(index<limit)
        {
            if(selfArray[index] == searchedArray[0])
            {
                check = index + 1
                look = 1

                while(look < searched.count)
                {
                    if(selfArray[check] != searchedArray[look])
                    {
                        break
                    }

                    look = look + 1
                    check = check + 1
                }

                if(look >= searched.count)
                {
                    return index
                }
            }

            index = index + 1
        }

        return -1
    }

    func substring(_ start : Int) -> String
    {
        return String(self[String.Index(encodedOffset:start) ..< String.Index(encodedOffset:self.count)])
    }

    func substring(_ start : Int, _ end : Int) -> String
    {
        return String(self[String.Index(encodedOffset:start) ..< String.Index(encodedOffset:end)])
    }

    func compareTo(_ other: String) -> Int
    {
        if(self < other)
        {
            return -1
        }

        if(self > other)
        {
            return 1
        }

        return 0
    }

    func compareTo(_ other: String, _ ignoreCase:Boolean) -> Int
    {
        if(ignoreCase)
        {
            let comparison = self.caseInsensitiveCompare(other)

            switch comparison
            {
                case ComparisonResult.orderedAscending : return -1
            	case ComparisonResult.orderedSame : return 0
            	case ComparisonResult.orderedDescending : return 1
           	}
        }

        return self.compareTo(other)
    }

    func equals(_ other: String, _ ignoreCase:Boolean) -> Boolean
    {
        return self.compareTo(other, ignoreCase) == 0
    }

    func trim() -> String
    {
        return self.trimmingCharacters(in: .whitespacesAndNewlines)
    }
}

public extension Int
{
   func toByte() -> Byte
   {
      return Byte(self & 0xFF)
   }

   func toLong() -> Long
   {
       return Long(self)
   }

   func toFloat() -> Float
   {
       return Float(self)
   }

   func toDouble() -> Double
   {
      return Double(self)
   }
}

public extension Byte
{
   func toInt() -> Int
   {
       return Int(self)
   }

   func toLong() -> Long
   {
       return Long(self)
   }

   func toFloat() -> Float
   {
       return Float(self)
   }

   func toDouble() -> Double
   {
      return Double(self)
   }
}

public extension Long
{
   func toByte() -> Byte
   {
      return Byte(self & 0xFF)
   }

   func toInt() -> Int
   {
      let value = self & Long(0xFFFFFFFF)

      if(value > Long(0x7F000000))
      {
        return Int(value - Long(0x100000000))
      }

      return Int(value)
   }

   func toFloat() -> Float
   {
       return Float(self)
   }

   func toDouble() -> Double
   {
      return Double(self)
   }
}

public extension Float
{
   func toInt() -> Int
   {
       return Int(self)
   }

   func toLong() -> Long
   {
       return Long(self)
   }

   func toDouble() -> Double
   {
       return Double(self)
   }
}


public extension Double
{
   static var POSITIVE_INFINITY : Double { get {  return 1.0 / 0.0} }
   static var NEGATIVE_INFINITY : Double { get {  return -1.0 / 0.0} }

   func toInt() -> Int
   {
       return Int(self)
   }

   func toLong() -> Long
   {
       return Long(self)
   }

   func toFloat() -> Float
   {
       return Float(self)
   }
}

public extension Array
{
    func firstOrNull(_ filter:(Element)->Bool) -> Element?
    {
        return self.first(where: filter)
    }
}

public class Math
{
    static let PI : Double = 3.14159265358979323846
    static let E : Double = 2.7182818284590452354

    static func random() -> Double
    {
        return Double.random(in: 0 ..< 1)
    }

    static func pow(_ number:Double, _ exponent:Double) -> Double
    {
        return Foundation.pow(number, exponent)
    }
}

func sqrt(_ number:Double) -> Double
{
     return number.squareRoot()
}

func sqrt(_ number:Float) -> Float
{
     return number.squareRoot()
}

public class Mutex
{
    private let mutex : DispatchSemaphore

    public init()
    {
        self.mutex = DispatchSemaphore(value : 1)
    }

    public func safeExecute(task:()->Void)
    {
        self.mutex.wait()
        task()
        self.mutex.signal()
    }


    public func safeExecuteMayThrows(_ task: @escaping () throws -> Void) throws
    {
        self.mutex.wait()
        var errorCollected : Error? = nil

        do
        {
            try task()
        }
        catch
        {
          errorCollected = error
        }

        self.mutex.signal()

        if errorCollected != nil
        {
            throw errorCollected!
        }
    }

}

public class Locker
{
    private let mutex : DispatchSemaphore

    public init()
    {
        self.mutex = DispatchSemaphore(value : 0)
    }

    public func lock()
    {
        self.mutex.wait()
    }

    public func unlock()
    {
        self.mutex.signal()
    }
}

public func timeSince1970InMilliseconds() -> Long
{
    return Long(NSDate().timeIntervalSince1970 * 1000.0)
}

public func fatal<T>(_ message:String) -> T
{
   fatalError(message)
}
