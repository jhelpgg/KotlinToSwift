import Foundation

public class ByteStream
{
    private var data : Array<Byte>
    private var index : Int
    private let bigEndian : Bool

    init(_ data : Array<Byte>, _ bigEndian:Bool)
    {
        self.bigEndian = bigEndian
        self.data = data
        self.index = 0
    }

    func remaining() -> Int
    {
        return self.data.count - self.index
    }

    func readInt8() -> Int
    {
        let data = self.readData(1)
        return Int(data.withUnsafeBytes { $0.load(fromByteOffset: 0, as: UInt8.self) })
    }

    func readInt16() -> Int
    {
        let data = self.readData(2)
        return Int(data.withUnsafeBytes { $0.load(fromByteOffset: 0, as: UInt16.self) })
    }

    func readInt32() -> Int
    {
        let data = self.readData(4)
        return Int(data.withUnsafeBytes { $0.load(fromByteOffset: 0, as: Int32.self) })
    }

    func readInt64() -> Long
    {
        let data = self.readData(8)
        return Long(data.withUnsafeBytes { $0.load(fromByteOffset: 0, as: Int64.self) })
    }

    func readFloat() -> Float
    {
        let data = self.readData(4)
        return data.withUnsafeBytes { $0.load(fromByteOffset: 0, as: Float.self) }
    }

    func readDouble() -> Double
    {
        let data = self.readData(8)
        return data.withUnsafeBytes { $0.load(fromByteOffset: 0, as: Double.self) }
    }

    private func readData(_ number:Int) -> Data
    {
        var array = [UInt8]()
        let max = self.index + number
        var byte : Byte

        while self.index < max
        {
            byte = self.data[self.index]

            if self.bigEndian
            {
                array.insert(UInt8(byte), at: 0)
            }
            else
            {
                array.append(UInt8(byte))
            }

            self.index += 1
        }

        var data = Data()
        data.append(contentsOf: array)
        return data
    }
}
